package mod.surviving_the_aftermath.capability;

import java.util.Optional;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.raid.NetherRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public class RaidData implements INBTSerializable<CompoundTag> {

	public static final Capability<RaidData> CAPABILITY = CapabilityManager.get(new CapabilityToken<RaidData>() {
	});

	private static final ResourceLocation STRUCTURE = new ResourceLocation(Main.MODID, "nether_invasion_portal");

	private ServerLevel level;
	private Optional<BlockPos> position = Optional.empty();
	private Optional<NetherRaid> raid = Optional.empty();

	public RaidData(ServerLevel level) {
		this.level = level;
	}

	public void generate() {
		if (position.isEmpty()) {
			var pos = level.getSharedSpawnPos();
			level.getStructureManager().get(STRUCTURE).ifPresent(template -> {
				template.placeInWorld(level, pos, pos,
						new StructurePlaceSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK), level.random,
						2);
			});

			position = level.getPoiManager().findClosest(h -> h.is(PoiTypes.NETHER_PORTAL), pos, 40,
					PoiManager.Occupancy.ANY);
		}
	}

	public boolean enterPortal(Entity entity) {
		return position.map(p -> {
			if (entity.blockPosition().distSqr(p) < 400) {
				if (entity instanceof Player && raid.isEmpty()) {
					raid = Optional.of(new NetherRaid(p, level));
				}
				return true;
			}
			return false;
		}).orElse(false);
	}

	public void tick() {
		raid.ifPresent(r -> r.tick(level));
	}

	@Override
	public CompoundTag serializeNBT() {
		var tag = new CompoundTag();
		position.ifPresent(p -> tag.put("position", NbtUtils.writeBlockPos(p)));
		raid.ifPresent(r -> tag.put("raid", NetherRaid.CODEC.encodeStart(NbtOps.INSTANCE, r).getOrThrow(false, s -> {
		})));
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		if (nbt.contains("position"))
			position = Optional.of(NbtUtils.readBlockPos(nbt.getCompound("position")));
		if (nbt.contains("raid"))
			raid = NetherRaid.CODEC.parse(NbtOps.INSTANCE, nbt.get("raid")).result();

	}

	public static LazyOptional<RaidData> get(Level level) {
		return level.getCapability(CAPABILITY);
	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		private LazyOptional<RaidData> instance;

		public Provider(ServerLevel level) {
			instance = LazyOptional.of(() -> new RaidData(level));
		}

		@Override
		public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
			return CAPABILITY.orEmpty(cap, instance);
		}

		@Override
		public CompoundTag serializeNBT() {
			return instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"))
					.serializeNBT();
		}

		@Override
		public void deserializeNBT(CompoundTag nbt) {
			instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!"))
					.deserializeNBT(nbt);
		}

	}
}
