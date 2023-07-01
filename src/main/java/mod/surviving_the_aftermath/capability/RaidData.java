package mod.surviving_the_aftermath.capability;

import java.util.Optional;

import mod.surviving_the_aftermath.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.phys.Vec3;
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

	private Level level;
	private Optional<BlockPos> position = Optional.empty();

	public RaidData(Level level) {
		this.level = level;
	}

	public void generate() {
		if (position.isEmpty() && level instanceof ServerLevel serverLevel) {
			var pos = level.getSharedSpawnPos();
			serverLevel.getStructureManager().get(STRUCTURE).ifPresent(template -> {
				template.placeInWorld(serverLevel, pos, pos,
						new StructurePlaceSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK), level.random,
						2);
			});

			position = serverLevel.getPoiManager().findClosest(h -> h.is(PoiTypes.NETHER_PORTAL), pos, 40,
					PoiManager.Occupancy.ANY);
		}
	}

	public boolean enterPortal(Entity entity) {
		return position.map(p -> {
			if (entity.blockPosition().distSqr(p) < 400) {
				if (entity instanceof Player) {
					var piglin = EntityType.PIGLIN.create(level);
					piglin.moveTo(Vec3.atCenterOf(p));
					piglin.setImmuneToZombification(true);
					level.addFreshEntity(piglin);
				}
				return true;
			}
			return false;
		}).orElse(false);
	}

	@Override
	public CompoundTag serializeNBT() {
		var tag = new CompoundTag();
		position.ifPresent(p -> tag.put("position", NbtUtils.writeBlockPos(p)));
		return tag;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		if (nbt.contains("position"))
			position = Optional.of(NbtUtils.readBlockPos(nbt.getCompound("position")));
	}

	public static LazyOptional<RaidData> get(Level level) {
		return level.getCapability(CAPABILITY);
	}

	public static class Provider implements ICapabilitySerializable<CompoundTag> {

		private LazyOptional<RaidData> instance;

		public Provider(Level level) {
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
