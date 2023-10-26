package com.pancake.surviving_the_aftermath;

import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.impl.amount.FixedAmountModule;
import com.pancake.surviving_the_aftermath.api.module.impl.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.api.module.impl.entity_info.BaseEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.impl.entity_info.EntityInfoWithEquipmentModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.EntityTypeWeightedListModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.data.AftermathModuleLoader;
import com.pancake.surviving_the_aftermath.raid.NetherRaid;
import com.pancake.surviving_the_aftermath.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.tracker.PlayerBattleTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(SurvivingTheAftermath.MOD_ID)
public class SurvivingTheAftermath {
    public static final String MOD_ID = "surviving_the_aftermath";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SurvivingTheAftermath() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.addListener(this::onDataPackLoad);
    }

    public static ResourceLocation asResource(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
    private void commonSetup(final FMLCommonSetupEvent event) {
        AftermathAPI instance = AftermathAPI.getInstance();
        instance.registerWeightedListModule(EntityTypeWeightedListModule.IDENTIFIER, EntityTypeWeightedListModule.class);
        instance.registerWeightedListModule(ItemWeightedListModule.IDENTIFIER, ItemWeightedListModule.class);

        instance.registerTracker(PlayerBattleTracker.IDENTIFIER,PlayerBattleTracker.class);


        instance.registerAmountModule(FixedAmountModule.IDENTIFIER,FixedAmountModule.class);
        instance.registerAmountModule(RandomAmountModule.IDENTIFIER,RandomAmountModule.class);

        instance.registerEntityInfoModule(BaseEntityInfoModule.IDENTIFIER,BaseEntityInfoModule.class);
        instance.registerEntityInfoModule(EntityInfoWithEquipmentModule.IDENTIFIER,EntityInfoWithEquipmentModule.class);

        instance.registerAftermathModule(NetherRaid.IDENTIFIER, BaseRaidModule.class);
    }

    @SubscribeEvent
    public void onDataPackLoad(AddReloadListenerEvent event) {
        event.addListener(new AftermathModuleLoader());
    }
}
