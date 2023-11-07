package com.pancake.surviving_the_aftermath.compat.kubejs;

import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEvents;
import dev.latvian.mods.kubejs.KubeJSPlugin;

public class ModKubeJSPlugin extends KubeJSPlugin {

    @Override
    public void registerEvents() {
        AftermathEvents.register();
    }
}
