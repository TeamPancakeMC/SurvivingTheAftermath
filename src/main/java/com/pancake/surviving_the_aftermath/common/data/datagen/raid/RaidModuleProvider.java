package com.pancake.surviving_the_aftermath.common.data.datagen.raid;

import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.data.datagen.AftermathModuleProviders;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
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
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RaidModuleProvider extends AftermathModuleProviders<BaseRaidModule> {
    public RaidModuleProvider(PackOutput output) {
        super(output, "Raid");
    }

    @Override
    public void addModules() {
        ItemWeightedModule rewards = new ItemWeightedModule.Builder()
                .add(Items.GOLD_INGOT,100)
                .add(Items.DIAMOND,10)
                .add(Items.EMERALD,20)
                .add(Items.ENCHANTED_GOLDEN_APPLE,2)
                .add(Items.NETHERITE_SCRAP,2)
                .build();

        ItemWeightedModule goldenEquipment = new ItemWeightedModule.Builder()
                .add(Items.GOLDEN_SWORD,1)
                .add(Items.GOLDEN_HELMET,1)
                .add(Items.GOLDEN_CHESTPLATE,1)
                .add(Items.GOLDEN_LEGGINGS,1)
                .add(Items.GOLDEN_BOOTS,1)
                .build();

        StructureConditionModule structureConditionModule = new StructureConditionModule(ModStructures.NETHER_RAID.location().toString());


        List<IEntityInfoModule> wave1 = Lists.newArrayList();
        wave1.add(new EntityInfoModule.Builder(EntityType.PIGLIN)
                .amountModule(new RandomAmountModule.Builder(4,5)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave2 = Lists.newArrayList();
        wave2.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new RandomAmountModule.Builder(4,5)
                        .build()
                )
                .build());
        wave2.add(new EntityInfoModule.Builder(EntityType.ZOMBIFIED_PIGLIN)
                .amountModule(new RandomAmountModule.Builder(4,5)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave3 = Lists.newArrayList();
        wave3.add(new EntityInfoModule.Builder(EntityType.PIGLIN)
                .amountModule(new RandomAmountModule.Builder(4,5)
                        .build()
                )
                .build());
        wave3.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new IntegerAmountModule.Builder(1)
                        .build()
                )
                .build());
        wave3.add(new EntityInfoModule.Builder(EntityType.MAGMA_CUBE)
                .amountModule(new IntegerAmountModule.Builder(1)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave4 = Lists.newArrayList();
        wave4.add(new EntityInfoModule.Builder(EntityType.PIGLIN)
                .amountModule(new RandomAmountModule.Builder(4,5)
                        .build()
                )
                .build());
        wave4.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new RandomAmountModule.Builder(1,2)
                        .build()
                )
                .build());
        wave4.add(new EntityInfoModule.Builder(EntityType.MAGMA_CUBE)
                .amountModule(new RandomAmountModule.Builder(1,2)
                        .build()
                )
                .build());
        wave4.add(new EntityInfoModule.Builder(EntityType.BLAZE)
                .amountModule(new IntegerAmountModule.Builder(1)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave5 = Lists.newArrayList();
        wave5.add(new EntityInfoModule.Builder(EntityType.PIGLIN)
                .amountModule(new RandomAmountModule.Builder(3,4)
                        .build()
                )
                .build());
        wave5.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new RandomAmountModule.Builder(1,2)
                        .build()
                )
                .build());
        wave5.add(new EntityInfoModule.Builder(EntityType.MAGMA_CUBE)
                .amountModule(new RandomAmountModule.Builder(1,2)
                        .build()
                )
                .build());
        wave5.add(new EntityInfoModule.Builder(EntityType.BLAZE)
                .amountModule(new IntegerAmountModule.Builder(1)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave6 = Lists.newArrayList();
        wave6.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(3,4)
                        .build()
                )
                .build());
        wave6.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new RandomAmountModule.Builder(2,3)
                        .build()
                )
                .build());
        wave6.add(new EntityInfoModule.Builder(EntityType.MAGMA_CUBE)
                .amountModule(new RandomAmountModule.Builder(1,2)
                        .build()
                )
                .build());
        wave6.add(new EntityInfoModule.Builder(EntityType.GHAST)
                .amountModule(new RandomAmountModule.Builder(1,3)
                        .build()
                )
                .build());
        wave6.add(new EntityInfoModule.Builder(EntityType.BLAZE)
                .amountModule(new IntegerAmountModule.Builder(1)
                        .build()
                )
                .build());
        wave6.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN_BRUTE)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(1,2)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave7 = Lists.newArrayList();
        wave7.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(3,4)
                        .build()
                )
                .build());
        wave7.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new RandomAmountModule.Builder(3,4)
                        .build()
                )
                .build());
        wave7.add(new EntityInfoModule.Builder(EntityType.MAGMA_CUBE)
                .amountModule(new RandomAmountModule.Builder(2,4)
                        .build()
                )
                .build());
        wave7.add(new EntityInfoModule.Builder(EntityType.GHAST)
                .amountModule(new RandomAmountModule.Builder(1,3)
                        .build()
                )
                .build());
        wave7.add(new EntityInfoModule.Builder(EntityType.BLAZE)
                .amountModule(new IntegerAmountModule.Builder(2)
                        .build()
                )
                .build());
        wave7.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN_BRUTE)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(1,2)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave8 = Lists.newArrayList();
        wave8.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(3,4)
                        .build()
                )
                .build());
        wave8.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new RandomAmountModule.Builder(3,4)
                        .build()
                )
                .build());
        wave8.add(new EntityInfoModule.Builder(EntityType.MAGMA_CUBE)
                .amountModule(new RandomAmountModule.Builder(2,4)
                        .build()
                )
                .build());
        wave8.add(new EntityInfoModule.Builder(EntityType.GHAST)
                .amountModule(new RandomAmountModule.Builder(1,5)
                        .build()
                )
                .build());
        wave8.add(new EntityInfoModule.Builder(EntityType.BLAZE)
                .amountModule(new IntegerAmountModule.Builder(4)
                        .build()
                )
                .build());
        wave8.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN_BRUTE)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(1,3)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave9 = Lists.newArrayList();
        wave9.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(3,6)
                        .build()
                )
                .build());
        wave9.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new RandomAmountModule.Builder(3,6)
                        .build()
                )
                .build());
        wave9.add(new EntityInfoModule.Builder(EntityType.MAGMA_CUBE)
                .amountModule(new RandomAmountModule.Builder(2,6)
                        .build()
                )
                .build());
        wave9.add(new EntityInfoModule.Builder(EntityType.GHAST)
                .amountModule(new RandomAmountModule.Builder(1,5)
                        .build()
                )
                .build());
        wave9.add(new EntityInfoModule.Builder(EntityType.BLAZE)
                .amountModule(new IntegerAmountModule.Builder(4)
                        .build()
                )
                .build());
        wave9.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN_BRUTE)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(3,6)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave10 = Lists.newArrayList();
        wave10.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(3,5)
                        .build()
                )
                .build());
        wave10.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new RandomAmountModule.Builder(3,5)
                        .build()
                )
                .build());
        wave10.add(new EntityInfoModule.Builder(EntityType.MAGMA_CUBE)
                .amountModule(new RandomAmountModule.Builder(2,5)
                        .build()
                )
                .build());
        wave10.add(new EntityInfoModule.Builder(EntityType.GHAST)
                .amountModule(new RandomAmountModule.Builder(1,5)
                        .build()
                )
                .build());
        wave10.add(new EntityInfoModule.Builder(EntityType.BLAZE)
                .amountModule(new IntegerAmountModule.Builder(4)
                        .build()
                )
                .build());
        wave10.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN_BRUTE)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.GOLDEN_SWORD,1)
                        .add(Items.GOLDEN_HELMET,1)
                        .add(Items.GOLDEN_CHESTPLATE,1)
                        .add(Items.GOLDEN_LEGGINGS,1)
                        .add(Items.GOLDEN_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(3,6)
                        .build()
                )
                .build());

        List<IEntityInfoModule> wave11 = Lists.newArrayList();
        wave11.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.NETHERITE_SWORD,1)
                        .add(Items.NETHERITE_HELMET,1)
                        .add(Items.NETHERITE_CHESTPLATE,1)
                        .add(Items.NETHERITE_LEGGINGS,1)
                        .add(Items.NETHERITE_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(5,10)
                        .build()
                )
                .build());
        wave11.add(new EntityInfoModule.Builder(EntityType.HOGLIN)
                .amountModule(new RandomAmountModule.Builder(5,10)
                        .build()
                )
                .build());
        wave11.add(new EntityInfoModule.Builder(EntityType.MAGMA_CUBE)
                .amountModule(new RandomAmountModule.Builder(4,10)
                        .build()
                )
                .build());
        wave11.add(new EntityInfoModule.Builder(EntityType.GHAST)
                .amountModule(new RandomAmountModule.Builder(3,10)
                        .build()
                )
                .build());
        wave11.add(new EntityInfoModule.Builder(EntityType.BLAZE)
                .amountModule(new IntegerAmountModule.Builder(5)
                        .build()
                )
                .build());
        wave11.add(new EntityInfoWithPredicateModule.Builder(EntityType.PIGLIN_BRUTE)
                .add(new EquipmentPredicate.Builder()
                        .add(Items.NETHERITE_SWORD,1)
                        .add(Items.NETHERITE_HELMET,1)
                        .add(Items.NETHERITE_CHESTPLATE,1)
                        .add(Items.NETHERITE_LEGGINGS,1)
                        .add(Items.NETHERITE_BOOTS,1)
                        .canDrop(false)
                        .build())
                .amountModule(new RandomAmountModule.Builder(5,10)
                        .build()
                )
                .build());





        BaseRaidModule netherRaidModule = new BaseRaidModule.Builder("common")
                .readyTime(100)
                .rewardTime(100)
                .rewards(rewards)
                .addWave(wave1)
                .addWave(wave2)
                .addWave(wave3)
                .addWave(wave4)
                .addWave(wave5)
                .addWave(wave6)
                .addWave(wave7)
                .addWave(wave8)
                .addWave(wave9)
                .addWave(wave10)
                .addWave(wave11)
                .addCondition(structureConditionModule)
                .build();


        addModule(netherRaidModule);
    }
}
