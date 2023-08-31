package mod.surviving_the_aftermath.event;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.capability.RaidData;
import mod.surviving_the_aftermath.init.ModStructures;
import mod.surviving_the_aftermath.init.ModTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ServerLevelData;
import net.minecraftforge.event.TickEvent.LevelTickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent.CreateSpawnPosition;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
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
}