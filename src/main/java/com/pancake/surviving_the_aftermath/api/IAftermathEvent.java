package com.pancake.surviving_the_aftermath.api;

import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraftforge.eventbus.api.Event;

public interface IAftermathEvent {
    Event getForge();
    EventJS getKubeJS();
}
