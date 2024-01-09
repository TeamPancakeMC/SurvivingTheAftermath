package com.pancake.surviving_the_aftermath;

import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.common.config.AftermathConfig;
import com.pancake.surviving_the_aftermath.common.data.datagen.EventSubscriber;
import com.pancake.surviving_the_aftermath.common.data.pack.AftermathModuleLoader;
import com.pancake.surviving_the_aftermath.common.event.subscriber.EnchantmentSubscriber;
import com.pancake.surviving_the_aftermath.common.init.*;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import com.pancake.surviving_the_aftermath.common.tracker.MobBattleTracker;
import com.pancake.surviving_the_aftermath.common.tracker.RaidMobBattleTracker;
import com.pancake.surviving_the_aftermath.common.tracker.RaidPlayerBattleTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.lang.reflect.Field;

@Mod(SurvivingTheAftermath.MOD_ID)
public class SurvivingTheAftermath {
    public static final String MOD_ID = "surviving_the_aftermath";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SurvivingTheAftermath() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        ModuleRegistry.register(bus);
        ModBlocks.BLOCKS.register(bus);
        ModItems.ITEMS.register(bus);
        ModTabs.TABS.register(bus);
        ModVillagers.VILLAGER_PROFESSIONS.register(bus);
        ModEnchantments.ENCHANTMENTS.register(bus);
        ModSoundEvents.SOUND_EVENTS.register(bus);
        ModStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(bus);
        ModStructureTypes.STRUCTURE_TYPES.register(bus);
        ModMobEffects.MOB_EFFECTS.register(bus);
        bus.addListener(EventSubscriber::onGatherData);
        bus.addListener(EnchantmentSubscriber::onFMLClientSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onDataPackLoad);


        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, AftermathConfig.SPEC);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    @SubscribeEvent
    public void onDataPackLoad(AddReloadListenerEvent event) {
        event.addListener(new AftermathModuleLoader());
    }

}
