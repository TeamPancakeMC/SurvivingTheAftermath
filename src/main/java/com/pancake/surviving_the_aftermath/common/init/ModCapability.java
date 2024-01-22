package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.capability.AftermathCap;
import com.pancake.surviving_the_aftermath.common.capability.AftermathStageCap;
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
    public static final Capability<AftermathStageCap> STAGE_CAP = CapabilityManager.get(new CapabilityToken<>() {});
    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(AftermathCap.class);
        event.register(AftermathStageCap.class);
    }

    @SubscribeEvent
    public static void attachLevelCapability(AttachCapabilitiesEvent<Level> event) {
        Level level = event.getObject();
        if (level.dimension() == Level.OVERWORLD && level instanceof ServerLevel serverLevel) {
            event.addCapability(SurvivingTheAftermath.asResource("aftermath_cap"), new AftermathCap.Provider(serverLevel));
        }
        event.addCapability(SurvivingTheAftermath.asResource("stage_cap"), new AftermathStageCap.Provider());
    }

    @SubscribeEvent
    public static void attachEntityCapability(AttachCapabilitiesEvent<Entity> event) {
        Entity entity = event.getObject();
        if (entity instanceof Player) {
            event.addCapability(SurvivingTheAftermath.asResource("stage_cap"), new AftermathStageCap.Provider());
        }
    }

}
