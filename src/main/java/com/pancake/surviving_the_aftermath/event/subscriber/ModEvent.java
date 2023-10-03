package com.pancake.surviving_the_aftermath.event.subscriber;

import com.pancake.surviving_the_aftermath.capability.AftermathCap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModEvent {
    @SubscribeEvent
    public static void onTickLevelTick(TickEvent.LevelTickEvent event) {
        Level level = event.level;
        if (level.isClientSide() || event.phase == TickEvent.Phase.END) return;
        AftermathCap.get(level).ifPresent(AftermathCap::tick);
    }
}
