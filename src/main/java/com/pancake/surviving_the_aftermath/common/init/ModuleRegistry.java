package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.module.*;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class ModuleRegistry {
    public static final DeferredRegister<IAftermath> AFTERMATH = DeferredRegister.create(Keys.AFTERMATH, SurvivingTheAftermath.MOD_ID);
    public static final Supplier<IForgeRegistry<IAftermath>> AFTERMATH_REGISTRY = AFTERMATH.makeRegistry(RegistryBuilder::new);
    public static final DeferredRegister<IAftermathModule> AFTERMATH_MODULE = DeferredRegister.create(Keys.AFTERMATH_MODULE, SurvivingTheAftermath.MOD_ID);
    public static final Supplier<IForgeRegistry<IAftermathModule>> AFTERMATH_MODULE_REGISTRY = AFTERMATH_MODULE.makeRegistry(RegistryBuilder::new);


    public static final DeferredRegister<IEntityInfoModule> ENTITY_INFO_MODULE = DeferredRegister.create(Keys.ENTITY_INFO, SurvivingTheAftermath.MOD_ID);
    public static final Supplier<IForgeRegistry<IEntityInfoModule>> ENTITY_INFO_REGISTRY = ENTITY_INFO_MODULE.makeRegistry(RegistryBuilder::new);

    public static final DeferredRegister<IAmountModule> AMOUNT_MODULE = DeferredRegister.create(Keys.AMOUNT, SurvivingTheAftermath.MOD_ID);
    public static final Supplier<IForgeRegistry<IAmountModule>> AMOUNT_REGISTRY = AMOUNT_MODULE.makeRegistry(RegistryBuilder::new);

    public static final DeferredRegister<IWeightedModule<?>> WEIGHTED_MODULE = DeferredRegister.create(Keys.WEIGHTED, SurvivingTheAftermath.MOD_ID);
    public static final Supplier<IForgeRegistry<IWeightedModule<?>>> WEIGHTED_REGISTRY = WEIGHTED_MODULE.makeRegistry(RegistryBuilder::new);

    public static final DeferredRegister<IConditionModule> CONDITION_MODULE = DeferredRegister.create(Keys.CONDITION, SurvivingTheAftermath.MOD_ID);
    public static final Supplier<IForgeRegistry<IConditionModule>> CONDITION_REGISTRY = CONDITION_MODULE.makeRegistry(RegistryBuilder::new);

    public static final DeferredRegister<IPredicateModule> PREDICATE_MODULE = DeferredRegister.create(Keys.PREDICATE, SurvivingTheAftermath.MOD_ID);
    public static final Supplier<IForgeRegistry<IPredicateModule>> PREDICATE_REGISTRY = PREDICATE_MODULE.makeRegistry(RegistryBuilder::new);


    public static final DeferredRegister<ITracker> TRACKER_MODULE = DeferredRegister.create(Keys.TRACKER, SurvivingTheAftermath.MOD_ID);
    public static final Supplier<IForgeRegistry<ITracker>> TRACKER_REGISTRY = TRACKER_MODULE.makeRegistry(RegistryBuilder::new);

    public static void register(IEventBus bus) {
        AFTERMATH.register(bus);
        AFTERMATH_MODULE.register(bus);
        AMOUNT_MODULE.register(bus);
        ENTITY_INFO_MODULE.register(bus);
        WEIGHTED_MODULE.register(bus);
        CONDITION_MODULE.register(bus);
        PREDICATE_MODULE.register(bus);
        TRACKER_MODULE.register(bus);
    }


    public static final class Keys {
        public static final ResourceKey<Registry<IAftermath>> AFTERMATH = key("aftermath");
        public static final ResourceKey<Registry<IAftermathModule>> AFTERMATH_MODULE = key("aftermath_module");
        public static final ResourceKey<Registry<IAmountModule>> AMOUNT = key("amount");
        public static final ResourceKey<Registry<IEntityInfoModule>> ENTITY_INFO = key("entity_info");
        public static final ResourceKey<Registry<IWeightedModule<?>>> WEIGHTED = key("weighted");
        public static final ResourceKey<Registry<IConditionModule>> CONDITION = key("condition");
        public static final ResourceKey<Registry<IPredicateModule>> PREDICATE = key("predicate");
        public static final ResourceKey<Registry<ITracker>> TRACKER = key("tracker");


        private static <T> ResourceKey<Registry<T>> key(String name)
        {
            return ResourceKey.createRegistryKey(SurvivingTheAftermath.asResource(name));
        }
    }
}
