package com.pancake.surviving_the_aftermath;

import com.pancake.surviving_the_aftermath.api.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.impl.weighted.EntityTypeWeightedListModule;
import com.pancake.surviving_the_aftermath.api.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.data.AftermathModuleLoader;
import com.pancake.surviving_the_aftermath.raid.NetherRaid;
import com.pancake.surviving_the_aftermath.raid.NetherRaidFactory;
import com.pancake.surviving_the_aftermath.raid.NetherRaidModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(SurvivingTheAftermath.MOD_ID)
public class SurvivingTheAftermath {
    public static final String MOD_ID = "surviving_the_aftermath";
    public static final String MOD_NAME = "SurvivingTheAftermath";
    public SurvivingTheAftermath() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        setup();
        MinecraftForge.EVENT_BUS.addListener(this::onDataPackLoad);
    }
    public static ResourceLocation asResource(String name) {
        return new ResourceLocation(MOD_ID, name);
    }

    public void setup() {
        AftermathAPI instance = AftermathAPI.getInstance();
        // Register the IWeightedListModule
        instance.registerWeightedListModule(EntityTypeWeightedListModule.IDENTIFIER,EntityTypeWeightedListModule.class);
        instance.registerWeightedListModule(ItemWeightedListModule.IDENTIFIER, ItemWeightedListModule.class);

        // Register the IAftermathModule
        instance.registerAftermathModule(NetherRaid.IDENTIFIER, NetherRaidModule.class);

        // Register the IAftermathFactory
        instance.registerAftermathFactory(NetherRaid.IDENTIFIER, NetherRaidFactory.class);
    }

    @SubscribeEvent
    public void onDataPackLoad(AddReloadListenerEvent event) {
        event.addListener(new AftermathModuleLoader());
    }
}
