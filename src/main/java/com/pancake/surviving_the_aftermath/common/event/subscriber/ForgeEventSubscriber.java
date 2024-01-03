package com.pancake.surviving_the_aftermath.common.event.subscriber;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathManager;
import com.pancake.surviving_the_aftermath.common.capability.AftermathCap;
import com.pancake.surviving_the_aftermath.common.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.event.level.LevelEvent.CreateSpawnPosition;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

import java.util.List;

@EventBusSubscriber(modid = SurvivingTheAftermath.MOD_ID, bus = Bus.FORGE)
public class ForgeEventSubscriber {
	@SubscribeEvent
	public static void changeSpawn(CreateSpawnPosition event) {
		if (event.getLevel() instanceof ServerLevel level) {
			ServerLevelData settings = event.getSettings();
			BlockPos pos = level.findNearestMapStructure(ModTags.NETHER_RAID,
					new BlockPos(settings.getXSpawn(), settings.getYSpawn(), settings.getZSpawn()), 100, false);
			if (pos != null) {
				settings.setSpawn(new BlockPos(pos.getX(), level.getHeight(Heightmap.Types.WORLD_SURFACE, pos.getX(), pos.getZ()), pos.getZ()), 0);
				event.setCanceled(true);
			}
		}
	}

	@SubscribeEvent
	public static void onTickLevelTick(TickEvent.LevelTickEvent event) {
		Level level = event.level;
		if (event.phase == TickEvent.Phase.END && !level.isClientSide()) {
			AftermathCap.get(level).ifPresent(AftermathCap::tick);

		}
	}
	@SubscribeEvent
	public static void onLevel(LevelEvent.Unload event) {
		if (event.getLevel().isClientSide()) return;
		AftermathManager.getInstance().getAftermathMap().clear();
	}
}