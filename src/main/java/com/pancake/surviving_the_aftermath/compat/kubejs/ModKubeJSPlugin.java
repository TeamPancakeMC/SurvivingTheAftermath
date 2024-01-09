package com.pancake.surviving_the_aftermath.compat.kubejs;


import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.script.BindingsEvent;

public class ModKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void registerEvents() {
        AftermathEvents.register();
    }

    public void registerBindings( BindingsEvent event ) {
//        event.add("FixedAmountModule.Builder", FixedAmountModule.Builder.class);
//        event.add("RandomAmountModule.Builder", FixedAmountModule.Builder.class);
//        event.add("EntityInfoModule.Builder",EntityInfoModule.Builder.class);
//        event.add("EntityInfoWithEquipmentModule.Builder", EntityInfoWithEquipmentModule.Builder.class);
//        event.add("EntityTypeWeightedListModule.Builder", EntityTypeWeightedModule.Builder.class);
//        event.add("ItemWeightedListModule.Builder", ItemWeightedModule.Builder.class);
//        event.add("NetherRaidModule.Builder", NetherRaidModule.Builder.class);
    }
}
