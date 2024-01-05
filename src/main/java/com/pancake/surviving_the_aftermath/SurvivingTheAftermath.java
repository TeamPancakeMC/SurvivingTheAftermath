package com.pancake.surviving_the_aftermath;

import com.mojang.logging.LogUtils;

import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SurvivingTheAftermath.MOD_ID)
public class SurvivingTheAftermath {
    public static final String MOD_ID = "surviving_the_aftermath";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SurvivingTheAftermath() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        bus.addListener(ModAftermathModule::onNewRegistry);
        ModAftermathModule.MODULE.register(bus);

    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

}
