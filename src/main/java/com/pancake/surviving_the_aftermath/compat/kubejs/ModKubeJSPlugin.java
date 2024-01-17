package com.pancake.surviving_the_aftermath.compat.kubejs;

import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.common.module.amount.*;
import com.pancake.surviving_the_aftermath.common.module.condition.*;
import com.pancake.surviving_the_aftermath.common.module.entity_info.*;
import com.pancake.surviving_the_aftermath.common.module.weighted.*;
import com.pancake.surviving_the_aftermath.common.raid.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEvents;
import com.pancake.surviving_the_aftermath.util.StructureUtils;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class ModKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void registerEvents() {
        AftermathEvents.register();
    }
    public void registerBindings(BindingsEvent event) {
        event.add("AftermathManager", AftermathManager.class);
        event.add("StructureUtils", StructureUtils.class);
        event.add("BaseRaid", BaseRaid.class);
        event.add("BaseRaidModule", BaseRaidModule.Builder.class);
        event.add("IntegerAmountModule", IntegerAmountModule.Builder.class);
        event.add("RandomAmountModule", RandomAmountModule.Builder.class);


        event.add("EntityInfoModule", EntityInfoModule.Builder.class);
        event.add("EntityInfoWithPredicateModule", EntityInfoWithPredicateModule.Builder.class);

        event.add("EntityTypeWeightedModule", EntityTypeWeightedModule.Builder.class);
        event.add("ItemWeightedModule", ItemWeightedModule.Builder.class);



        event.add("BiomesConditionModule", BiomesConditionModule.Builder.class);
        event.add("StructureConditionModule", StructureConditionModule.Builder.class);
        event.add("WeatherConditionModule", WeatherConditionModule.Builder.class);
        event.add("YAxisHeightConditionModule", YAxisHeightConditionModule.Builder.class);
        event.add("XpConditionModule", XpConditionModule.Builder.class);
    }
}