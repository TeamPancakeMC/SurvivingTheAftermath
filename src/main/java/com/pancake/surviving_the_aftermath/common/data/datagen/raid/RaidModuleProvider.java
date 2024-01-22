package com.pancake.surviving_the_aftermath.common.data.datagen.raid;

import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.data.datagen.AftermathModuleProviders;
import com.pancake.surviving_the_aftermath.common.module.amount.IntegerAmountModule;
import com.pancake.surviving_the_aftermath.common.module.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.common.module.condition.LevelStageConditionModule;
import com.pancake.surviving_the_aftermath.common.module.condition.PlayerStageConditionModule;
import com.pancake.surviving_the_aftermath.common.module.condition.StructureConditionModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoWithPredicateModule;
import com.pancake.surviving_the_aftermath.common.module.predicate.AttributePredicate;
import com.pancake.surviving_the_aftermath.common.module.predicate.EffectPredicate;
import com.pancake.surviving_the_aftermath.common.module.predicate.EquipmentPredicate;
import com.pancake.surviving_the_aftermath.common.module.predicate.NBTPredicate;
import com.pancake.surviving_the_aftermath.common.module.weighted.AttributeWeightedModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import net.minecraft.data.PackOutput;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Items;

import java.util.List;

public class RaidModuleProvider extends AftermathModuleProviders<BaseRaidModule> {
    public RaidModuleProvider(PackOutput output) {
        super(output, "Raid");
    }

    @Override
    public void addModules() {
        RandomAmountModule random12 = new RandomAmountModule.Builder(1, 2).build();
        RandomAmountModule random23 = new RandomAmountModule.Builder(2, 3).build();
        RandomAmountModule random34 = new RandomAmountModule.Builder(3, 4).build();
        RandomAmountModule random45 = new RandomAmountModule.Builder(4, 5).build();
        RandomAmountModule random56 = new RandomAmountModule.Builder(5, 6).build();

        IntegerAmountModule integer1 = new IntegerAmountModule.Builder(1).build();
        IntegerAmountModule integer2 = new IntegerAmountModule.Builder(2).build();
        IntegerAmountModule integer3 = new IntegerAmountModule.Builder(3).build();
        IntegerAmountModule integer4 = new IntegerAmountModule.Builder(4).build();
        IntegerAmountModule integer5 = new IntegerAmountModule.Builder(5).build();


        ItemWeightedModule itemGoldenModule = new ItemWeightedModule.Builder()
                .add("minecraft:golden_helmet", 1)
                .add("minecraft:golden_chestplate", 1)
                .add("minecraft:golden_leggings", 1)
                .add("minecraft:golden_boots", 1)
                .add("minecraft:golden_sword", 1)
                .build();

        List<IEntityInfoModule> waves1 = List.of(
                new EntityInfoModule.Builder("minecraft:piglin")
                        .amountModule(integer1)
                        .build(),
                new EntityInfoModule.Builder("minecraft:hoglin")
                        .amountModule(random12)
                        .build()
        );

        List<IEntityInfoModule> waves2 = List.of(
                new EntityInfoWithPredicateModule.Builder("minecraft:piglin_brute")
                        .add(new EffectPredicate.Builder()
                                .add(new MobEffectInstance(MobEffects.MOVEMENT_SPEED,100,2),1)
                                .add(new MobEffectInstance(MobEffects.LEVITATION,100,2),1)
                                .build()
                        )
                        .add(new AttributePredicate.Builder()
                                .add(new AttributeWeightedModule.AttributeInfo(Attributes.MAX_HEALTH,
                                                new AttributeModifier("nether_raid", 5.0, AttributeModifier.Operation.ADDITION))
                                        ,1)
                                .build()
                        )
                        .amountModule(random23)
                        .build(),
                new EntityInfoWithPredicateModule.Builder("minecraft:creeper")
                        .add(new NBTPredicate()
                                .add("powered",1)
                        )
                        .amountModule(integer1)
                        .build(),
                new EntityInfoWithPredicateModule.Builder("minecraft:piglin")
                        .add(new EquipmentPredicate.Builder()
                                .canDrop(false)
                                .add("minecraft:golden_helmet", 1)
                                .add("minecraft:golden_chestplate", 1)
                                .add("minecraft:golden_leggings", 1)
                                .add("minecraft:golden_boots", 1)
                                .add("minecraft:golden_sword", 1)
                                .build())
                        .amountModule(integer1)
                        .build()
        );


        BaseRaidModule netherRaid = new BaseRaidModule.Builder("nether_raid")
                .readyTime(100)
                .rewardTime(100)
                .addWave(waves1)
                .addWave(waves2)
                .rewards(itemGoldenModule)
                .addCondition(new StructureConditionModule("surviving_the_aftermath:nether_invasion_portal"))
                .addCondition(new LevelStageConditionModule("111"))
                .build();


        addModule(netherRaid);

    }
}
