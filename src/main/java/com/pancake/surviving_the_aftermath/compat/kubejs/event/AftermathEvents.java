package com.pancake.surviving_the_aftermath.compat.kubejs.event;

import com.pancake.surviving_the_aftermath.compat.kubejs.ModExtra;
import dev.latvian.mods.kubejs.event.EventGroup;
import dev.latvian.mods.kubejs.event.EventHandler;

public interface AftermathEvents {
    EventGroup GROUP = EventGroup.of("AftermathEvents");

    //Start
    EventHandler START = GROUP.server("start", () -> AftermathEventJS.StartJS.class);

    //End
    EventHandler END = GROUP.server("end", () -> AftermathEventJS.EndJS.class);

    //Ready
    EventHandler READY = GROUP.server("ready", () -> AftermathEventJS.ReadyJS.class);

    //Ongoing
    EventHandler ONGOING = GROUP.server("ongoing", () -> AftermathEventJS.OngoingJS.class);

    //Victory
    EventHandler VICTORY = GROUP.server("victory", () -> AftermathEventJS.VictoryJS.class);

    //Lose
    EventHandler LOSE = GROUP.server("lose", () -> AftermathEventJS.LoseJS.class);

    //Celebrating
    EventHandler CELEBRATING = GROUP.server("celebrating", () -> AftermathEventJS.CelebratingJS.class);

    EventHandler MODIFY = GROUP.startup("modify", () -> AftermathModifyEventJS.class).extra(ModExtra.AFTERMATH_TYPE);

    static void register() {
        GROUP.register();
    }

}
