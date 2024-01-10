package com.pancake.surviving_the_aftermath.common.init;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.module.*;
import com.pancake.surviving_the_aftermath.common.module.amount.IntegerAmountModule;
import com.pancake.surviving_the_aftermath.common.module.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.common.module.condition.StageConditionModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoWithEquipmentModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.EntityTypeWeightedModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import com.pancake.surviving_the_aftermath.common.tracker.RaidPlayerBattleTracker;
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


    public static final RegistryObject<IConditionModule> STAGE_CONDITION = ModuleRegistry.CONDITION_MODULE.register("stage", StageConditionModule::new);


    public static final RegistryObject<IAftermathModule> NETHER_RAID_MODULE = ModuleRegistry.AFTERMATH_MODULE.register(NetherRaid.IDENTIFIER, NetherRaidModule::new);
    public static final RegistryObject<IAftermath<IAftermathModule>> NETHER_RAID = ModuleRegistry.AFTERMATH.register(NetherRaid.IDENTIFIER, NetherRaid::new);

    public static final RegistryObject<ITracker> RAID_PLAYER_BATTLE_TRACKER = ModuleRegistry.TRACKER_MODULE.register("raid_player_battle_tracker", RaidPlayerBattleTracker::new);
}
