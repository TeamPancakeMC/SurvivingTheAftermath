package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.capability.AftermathCap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModCapability{
    public static final Capability<AftermathCap> AFTERMATH_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(AftermathCap.class);
    }

    @SubscribeEvent
    public static void attachLevelCapability(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject().dimension() == Level.OVERWORLD && event.getObject() instanceof ServerLevel level) {
            event.addCapability(SurvivingTheAftermath.asResource("aftermath_cap"), new AftermathCap.Provider(level));
        }
    }

    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof Player player) {

        }
    }

}

