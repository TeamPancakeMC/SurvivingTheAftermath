package mod.surviving_the_aftermath.event;

import java.util.HashSet;
import java.util.Set;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mod.surviving_the_aftermath.Main;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.PlayerLoggedInEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
public class ForgeEventSubscriber {

	private static final Set<Integer> RAIDS = new HashSet<>();

	@SubscribeEvent
	public static void raidCompleted(TickEvent.PlayerTickEvent event) {
		var player = event.player;
		if (player.level() instanceof ServerLevel level) {
			var raid = level.getRaidAt(player.blockPosition());
			if (raid != null && raid.isVictory() && !RAIDS.contains(raid.getId())) {
				var villager = EntityType.VILLAGER.create(level);
				var allay = EntityType.ALLAY.create(level);
				villager.moveTo(player.position());
				allay.moveTo(player.position());
				level.addFreshEntity(villager);
				level.addFreshEntity(allay);
				RAIDS.add(raid.getId());
			}
		}
	}

	@SubscribeEvent
	public static void generateStructure(PlayerLoggedInEvent event) {
		if (event.getEntity().level() instanceof ServerLevel level) {
			var data = level.getDataStorage().computeIfAbsent(nbt -> SpawnStructure.CODEC
					.parse(NbtOps.INSTANCE, nbt.get("SpawnStructure")).getOrThrow(false, s -> {
					}), () -> new SpawnStructure(false), Main.MODID + "_SpawnStructure");
			data.generate(level);
		}
	}

	private static class SpawnStructure extends SavedData {

		private static final ResourceLocation STRUCTURE = new ResourceLocation(Main.MODID, "nether_invasion_portal");

		public static final Codec<SpawnStructure> CODEC = RecordCodecBuilder
				.create(instance -> instance.group(Codec.BOOL.fieldOf("created").forGetter(SpawnStructure::isCreated))
						.apply(instance, SpawnStructure::new));

		private boolean created;

		public SpawnStructure(boolean created) {
			this.created = created;
		}

		public boolean isCreated() {
			return created;
		}

		private void generate(ServerLevel level) {
			if (created || !level.dimension().equals(Level.OVERWORLD))
				return;

			created = true;
			setDirty();

			level.getStructureManager().get(STRUCTURE).ifPresent(template -> {
				var pos = level.getSharedSpawnPos();
				template.placeInWorld(level, pos, pos,
						new StructurePlaceSettings().addProcessor(BlockIgnoreProcessor.STRUCTURE_BLOCK), level.random,
						2);
			});
		}

		@Override
		public CompoundTag save(CompoundTag pCompoundTag) {
			pCompoundTag.put("SpawnStructure", CODEC.encodeStart(NbtOps.INSTANCE, this).getOrThrow(false, s -> {
			}));
			return pCompoundTag;
		}
	}
}
