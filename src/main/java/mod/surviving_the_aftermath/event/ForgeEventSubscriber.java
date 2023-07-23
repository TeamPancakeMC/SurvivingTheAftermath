package mod.surviving_the_aftermath.event;

import java.util.HashSet;
import java.util.Set;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.capability.RaidData;
import mod.surviving_the_aftermath.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.level.LevelEvent.CreateSpawnPosition;
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
				player.sendSystemMessage(Component.translate("raid.surviving_the_aftermath.end"));
			}
		}
	}

	@SubscribeEvent
	public static void netherRaid(EntityTravelToDimensionEvent event) {
		event.setCanceled(
				RaidData.get(event.getEntity().level()).map(c -> c.enterPortal(event.getEntity())).orElse(false));
	}

	@SubscribeEvent
	public static void tickRaid(LevelTickEvent event) {
		RaidData.get(event.level).ifPresent(c -> c.tick());
	}

	@SubscribeEvent
	public static void joinRaid(EntityJoinLevelEvent event) {
		RaidData.get(event.getLevel()).ifPresent(c -> c.joinRaid(event.getEntity()));
	}

	@SubscribeEvent
	public static void changeSpawn(CreateSpawnPosition event) {
		if (event.getLevel() instanceof ServerLevel level) {
			var settings = event.getSettings();
			var pos = level.findNearestMapStructure(ModTags.NETHER_RAID,
					new BlockPos(settings.getXSpawn(), settings.getYSpawn(), settings.getZSpawn()), 100, false);
			if (pos != null) {
				settings.setSpawn(new BlockPos(pos.getX(),
						level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()), pos.getZ()), 0);
				event.setCanceled(true);
			}
		}
	}

	public static final ResourceLocation RAID_DATA_LOCATION = new ResourceLocation(Main.MODID, "raiddata");

	@SubscribeEvent
	public static void attachCapability(AttachCapabilitiesEvent<Level> event) {
		if (event.getObject().dimension() == Level.OVERWORLD && event.getObject() instanceof ServerLevel level)
			event.addCapability(RAID_DATA_LOCATION, new RaidData.Provider(level));
	}

}
