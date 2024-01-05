package com.pancake.surviving_the_aftermath.common.init;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.IModule;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.common.module.amount.IntegerAmountModule;
import com.pancake.surviving_the_aftermath.common.module.amount.RandomAmountModule;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;

import java.util.Map;
import java.util.function.Supplier;

public class ModAftermathModule {
    public static final ResourceKey<Registry<IAmountModule>> MODULE_KEY = ResourceKey.createRegistryKey(SurvivingTheAftermath.asResource("module"));
    public static final DeferredRegister<IAmountModule> MODULE = DeferredRegister.create(MODULE_KEY, SurvivingTheAftermath.MOD_ID);

    public static final RegistryObject<IAmountModule> INTEGER_AMOUNT =  MODULE.register(IntegerAmountModule.IDENTIFIER, IntegerAmountModule::new);
    public static final RegistryObject<IAmountModule> RANDOM_AMOUNT =  MODULE.register(RandomAmountModule.IDENTIFIER, RandomAmountModule::new);



    @SubscribeEvent
    public static void onNewRegistry(NewRegistryEvent event) {
        event.create(new RegistryBuilder<>().setName(MODULE_KEY.location()));
    }

}
