package mod.surviving_the_aftermath.event;

import mod.surviving_the_aftermath.capability.RaidData;
import mod.surviving_the_aftermath.init.ModStructures;
import mod.surviving_the_aftermath.raid.NetherRaid;
import mod.surviving_the_aftermath.raid.RaidManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.UUID;

@Mod.EventBusSubscriber
public class RaidCreateEventSubscriber {
    private static final RaidManager instance = RaidManager.getInstance();

    //NetherRaid
    @SubscribeEvent
    public static void onBlock(BlockEvent.PortalSpawnEvent event) {
        Level level = (Level) event.getLevel();
        if (level instanceof ServerLevel serverLevel) {
            NetherRaid netherRaid = new NetherRaid(serverLevel, UUID.randomUUID(), event.getPos());
            if (netherRaid.create()) {
                instance.create(netherRaid);
            } else {
                System.out.println("NetherRaid create failed");
            }
        }
    }

    @SubscribeEvent
    public static void netherRaid(EntityTravelToDimensionEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        BlockPos pos = entity.blockPosition();
        if (level instanceof ServerLevel serverLevel) {
            event.setCanceled(serverLevel.structureManager().getAllStructuresAt(pos).containsKey(level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID)));
        }
    }


    //tick

    @SubscribeEvent
    public static void onTickLevelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (event.phase == TickEvent.Phase.END && !level.isClientSide()) {
            RaidData.get(level).ifPresent(raidData -> raidData.tick((ServerLevel) level));
        }
    }

    //EntityJoin
    @SubscribeEvent
    public static void joinRaid(EntityJoinLevelEvent event) {
        instance.joinRaid(event.getEntity());
    }


    @SubscribeEvent
    public static void onLevel(LevelEvent.Unload event) {
//        ServerLevel level = (ServerLevel) event.getLevel();
        if (event.getLevel().isClientSide()) return;
        System.out.println("世界卸载");
        instance.clear();
    }
}
