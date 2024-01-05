package com.pancake.surviving_the_aftermath.common.init;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.module.amount.IntegerAmountModule;
import com.pancake.surviving_the_aftermath.common.module.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.common.module.condition.StageConditionModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoWithEquipmentModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.EntityTypeWeightedModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.*;

import java.util.function.Supplier;

public class ModAftermathModule {
    public static final RegistryObject<IEntityInfoModule> ENTITY_INFO = ModuleRegistry.ENTITY_INFO_MODULE.register(EntityInfoModule.IDENTIFIER,EntityInfoModule::new);
    public static final RegistryObject<IEntityInfoModule> ENTITY_INFO_EQUIPMENT = ModuleRegistry.ENTITY_INFO_MODULE.register(EntityInfoWithEquipmentModule.IDENTIFIER,EntityInfoWithEquipmentModule::new);


    public static final RegistryObject<IAmountModule> INTEGER_AMOUNT =  ModuleRegistry.AMOUNT_MODULE.register(IntegerAmountModule.IDENTIFIER, IntegerAmountModule::new);
    public static final RegistryObject<IAmountModule> RANDOM_AMOUNT =  ModuleRegistry.AMOUNT_MODULE.register(RandomAmountModule.IDENTIFIER, RandomAmountModule::new);


    public static final RegistryObject<IWeightedModule<EntityType<?>>> ENTITY_TYPE_WEIGHTED = ModuleRegistry.WEIGHTED_MODULE.register(EntityTypeWeightedModule.IDENTIFIER, EntityTypeWeightedModule::new);
    public static final RegistryObject<IWeightedModule<Item>> ITEM_WEIGHTED = ModuleRegistry.WEIGHTED_MODULE.register(ItemWeightedModule.IDENTIFIER, ItemWeightedModule::new);


    public static final RegistryObject<IConditionModule> STAGE_CONDITION = ModuleRegistry.CONDITION_MODULE.register("stage", StageConditionModule::new);



}
