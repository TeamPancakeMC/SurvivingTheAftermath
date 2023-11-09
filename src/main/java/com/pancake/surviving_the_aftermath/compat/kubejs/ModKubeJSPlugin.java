package com.pancake.surviving_the_aftermath.compat.kubejs;

import com.pancake.surviving_the_aftermath.api.module.impl.amount.FixedAmountModule;
import com.pancake.surviving_the_aftermath.api.module.impl.entity_info.EntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.impl.entity_info.EntityInfoWithEquipmentModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.EntityTypeWeightedListModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class ModKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void registerEvents() {
        AftermathEvents.register();
    }

    public void registerBindings( BindingsEvent event ) {
        event.add("FixedAmountModule.Builder", FixedAmountModule.Builder.class);
        event.add("RandomAmountModule.Builder", FixedAmountModule.Builder.class);
        event.add("EntityInfoModule.Builder",EntityInfoModule.Builder.class);
        event.add("EntityInfoWithEquipmentModule.Builder", EntityInfoWithEquipmentModule.Builder.class);
        event.add("EntityTypeWeightedListModule.Builder", EntityTypeWeightedListModule.Builder.class);
        event.add("ItemWeightedListModule.Builder", ItemWeightedListModule.Builder.class);
        event.add("NetherRaidModule.Builder", NetherRaidModule.Builder.class);
    }
}
