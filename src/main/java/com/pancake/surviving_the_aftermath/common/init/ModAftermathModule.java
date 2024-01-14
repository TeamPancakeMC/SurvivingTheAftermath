package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.module.*;
import com.pancake.surviving_the_aftermath.common.module.amount.IntegerAmountModule;
import com.pancake.surviving_the_aftermath.common.module.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.common.module.condition.*;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoWithEquipmentModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.EntityTypeWeightedModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber
public class ModAftermathModule {
    public static final RegistryObject<IEntityInfoModule> ENTITY_INFO = ModuleRegistry.ENTITY_INFO_MODULE.register(EntityInfoModule.IDENTIFIER,EntityInfoModule::new);
    public static final RegistryObject<IEntityInfoModule> ENTITY_INFO_EQUIPMENT = ModuleRegistry.ENTITY_INFO_MODULE.register(EntityInfoWithEquipmentModule.IDENTIFIER,EntityInfoWithEquipmentModule::new);


    public static final RegistryObject<IAmountModule> INTEGER_AMOUNT =  ModuleRegistry.AMOUNT_MODULE.register(IntegerAmountModule.IDENTIFIER, IntegerAmountModule::new);
    public static final RegistryObject<IAmountModule> RANDOM_AMOUNT =  ModuleRegistry.AMOUNT_MODULE.register(RandomAmountModule.IDENTIFIER, RandomAmountModule::new);


    public static final RegistryObject<IWeightedModule<EntityType<?>>> ENTITY_TYPE_WEIGHTED = ModuleRegistry.WEIGHTED_MODULE.register(EntityTypeWeightedModule.IDENTIFIER, EntityTypeWeightedModule::new);
    public static final RegistryObject<IWeightedModule<Item>> ITEM_WEIGHTED = ModuleRegistry.WEIGHTED_MODULE.register(ItemWeightedModule.IDENTIFIER, ItemWeightedModule::new);


//    public static final RegistryObject<IAftermathModule> NETHER_RAID_MODULE = ModuleRegistry.AFTERMATH_MODULE.register(NetherRaid.IDENTIFIER, NetherRaidModule::new);
    public static final RegistryObject<IAftermathModule> RAID_MODULE = ModuleRegistry.AFTERMATH_MODULE.register(BaseRaid.IDENTIFIER, BaseRaidModule::new);

    public static final RegistryObject<IAftermath<IAftermathModule>> BASE_RAID = ModuleRegistry.AFTERMATH.register(BaseRaid.IDENTIFIER, BaseRaid::new);


    public static final RegistryObject<IConditionModule> LEVEL_CONDITION = ModuleRegistry.CONDITION_MODULE.register(StructureConditionModule.IDENTIFIER, StructureConditionModule::new);
    public static final RegistryObject<IConditionModule> BIOMES_CONDITION = ModuleRegistry.CONDITION_MODULE.register(BiomesConditionModule.IDENTIFIER, BiomesConditionModule::new);
    public static final RegistryObject<IConditionModule> Y_AXIS_HEIGHT_CONDITION = ModuleRegistry.CONDITION_MODULE.register(YAxisHeightConditionModule.IDENTIFIER, YAxisHeightConditionModule::new);
    public static final RegistryObject<IConditionModule> WEATHER_CONDITION = ModuleRegistry.CONDITION_MODULE.register(WeatherConditionModule.IDENTIFIER, WeatherConditionModule::new);
    public static final RegistryObject<IConditionModule> XP_CONDITION = ModuleRegistry.CONDITION_MODULE.register(XpConditionModule.IDENTIFIER, XpConditionModule::new);
}
