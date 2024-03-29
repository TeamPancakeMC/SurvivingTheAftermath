package com.pancake.surviving_the_aftermath.compat.kubejs;

import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.common.capability.AftermathStageCap;
import com.pancake.surviving_the_aftermath.common.module.amount.*;
import com.pancake.surviving_the_aftermath.common.module.condition.*;
import com.pancake.surviving_the_aftermath.common.module.entity_info.*;
import com.pancake.surviving_the_aftermath.common.module.predicate.*;
import com.pancake.surviving_the_aftermath.common.module.weighted.*;
import com.pancake.surviving_the_aftermath.common.raid.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEvents;
import com.pancake.surviving_the_aftermath.common.util.StructureUtils;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;
import net.minecraft.resources.ResourceLocation;

public class ModKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void registerEvents() {
        AftermathEvents.register();
    }
    public void registerBindings(BindingsEvent event) {
        event.add("AftermathManager", AftermathManager.class);
        event.add("StructureUtils", StructureUtils.class);
        event.add("AftermathStageCap", AftermathStageCap.class);
        event.add("ResourceLocation", ResourceLocation.class);
        event.add("BaseRaid", BaseRaid.class);
        event.add("BaseRaidModule", BaseRaidModule.Builder.class);



        event.add("IntegerAmountModule", IntegerAmountModule.Builder.class);
        event.add("RandomAmountModule", RandomAmountModule.Builder.class);


        event.add("EntityInfoModule", EntityInfoModule.Builder.class);
        event.add("EntityInfoWithPredicateModule", EntityInfoWithPredicateModule.Builder.class);

        event.add("EntityTypeWeightedModule", EntityTypeWeightedModule.Builder.class);
        event.add("ItemWeightedModule", ItemWeightedModule.Builder.class);
        event.add("AttributeWeightedModule", AttributeWeightedModule.Builder.class);
        event.add("EffectWeightedModule", EffectWeightedModule.Builder.class);



        event.add("BiomesConditionModule", BiomesConditionModule.Builder.class);
        event.add("StructureConditionModule", StructureConditionModule.Builder.class);
        event.add("WeatherConditionModule", WeatherConditionModule.Builder.class);
        event.add("YAxisHeightConditionModule", YAxisHeightConditionModule.Builder.class);
        event.add("XpConditionModule", XpConditionModule.Builder.class);
        event.add("LevelStageConditionModule", LevelStageConditionModule.Builder.class);
        event.add("PlayerStageConditionModule", PlayerStageConditionModule.Builder.class);



        event.add("AttributePredicate", AttributePredicate.Builder.class);
        event.add("EffectPredicate", EffectPredicate.Builder.class);
        event.add("EquipmentPredicate", EquipmentPredicate.Builder.class);
        event.add("NBTPredicate", NBTPredicate.Builder.class);
    }
}