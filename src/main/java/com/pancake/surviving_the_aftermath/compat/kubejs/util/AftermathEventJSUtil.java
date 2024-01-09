package com.pancake.surviving_the_aftermath.compat.kubejs.util;

import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEventJS;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEvents;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathModifyEventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AftermathEventJSUtil {
    public static boolean start(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.START.post(new AftermathEventJS.StartJS(aftermath, players, level)).pass();
    }
    //post ready
    public static boolean ready(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.READY.post(new AftermathEventJS.ReadyJS(aftermath, players, level)).pass();
    }
    public static boolean ongoing(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.ONGOING.post(new AftermathEventJS.OngoingJS(aftermath, players, level)).pass();
    }
    public static boolean victory(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.VICTORY.post(new AftermathEventJS.VictoryJS(aftermath, players, level)).pass();
    }
    public static boolean celebrating(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.CELEBRATING.post(new AftermathEventJS.CelebratingJS(aftermath, players, level)).pass();
    }
    public static boolean lose(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.LOSE.post(new AftermathEventJS.LoseJS(aftermath, players, level)).pass();
    }
    public static boolean end(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.END.post(new AftermathEventJS.EndJS(aftermath, players, level)).pass();
    }

    public static boolean modify(String identifier, Set<IAftermathModule> aftermathModules) {
        return AftermathEvents.MODIFY.post(new AftermathModifyEventJS(identifier, aftermathModules)).pass();
    }
}
