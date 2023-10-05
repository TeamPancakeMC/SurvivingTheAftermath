package com.pancake.surviving_the_aftermath.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.capability.AftermathCap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModCapability {
    public static final Capability<AftermathCap> AFTERMATH_CAP = CapabilityManager.get(new CapabilityToken<>() {});

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(AftermathCap.class);
    }

    @SubscribeEvent
    public static void attachCapability(AttachCapabilitiesEvent<Level> event) {
        if (event.getObject().dimension() == Level.OVERWORLD && event.getObject() instanceof ServerLevel level) {
            event.addCapability(SurvivingTheAftermath.asResource("aftermath_cap"), new AftermathCap.Provider(level));
        }
    }
}

