package com.pancake.surviving_the_aftermath;

import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.impl.amount.FixedAmountModule;
import com.pancake.surviving_the_aftermath.api.module.impl.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.api.module.impl.entity_info.BaseEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.impl.entity_info.EntityInfoWithEquipmentModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.EntityTypeWeightedListModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.common.data.datagen.EventSubscriber;
import com.pancake.surviving_the_aftermath.common.data.pack.AftermathModuleLoader;
import com.pancake.surviving_the_aftermath.common.init.ModStructurePieceTypes;
import com.pancake.surviving_the_aftermath.common.init.ModStructureTypes;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import com.pancake.surviving_the_aftermath.common.tracker.PlayerBattleTracker;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

import java.lang.reflect.Field;

@Mod(SurvivingTheAftermath.MOD_ID)
public class SurvivingTheAftermath {
    public static final String MOD_ID = "surviving_the_aftermath";
    public static final Logger LOGGER = LogUtils.getLogger();
    public SurvivingTheAftermath() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(EventSubscriber::onGatherData);
        ModStructureTypes.STRUCTURE_TYPES.register(modEventBus);
        ModStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(modEventBus);
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

        instance.registerAftermathModule(NetherRaid.IDENTIFIER, NetherRaidModule.class);
        instance.registerAftermathFactory(NetherRaid.IDENTIFIER, NetherRaid.Factory.class);
    }

    @SubscribeEvent
    public void onDataPackLoad(AddReloadListenerEvent event) {
        event.addListener(new AftermathModuleLoader());
    }

    public static  <T> T getPrivateField(Object object, String fieldName, Class<T> fieldType) throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        return fieldType.cast(field.get(object));
    }
}
