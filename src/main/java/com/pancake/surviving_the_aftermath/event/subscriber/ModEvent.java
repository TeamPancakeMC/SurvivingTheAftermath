package com.pancake.surviving_the_aftermath.event.subscriber;

import com.pancake.surviving_the_aftermath.api.aftermath.AftermathManager;
import com.pancake.surviving_the_aftermath.capability.AftermathCap;;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModEvent {
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
        AftermathManager instance = AftermathManager.getInstance();
        instance.getAftermathMap().clear();
    }

    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) return;
//        NetherRaid netherRaid = new NetherRaid((ServerLevel) level, event.getPos());
//        AftermathManager instance = AftermathManager.getInstance();
//        instance.create(netherRaid);
    }
}
