package com.pancake.surviving_the_aftermath.compat.kubejs.util;

import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEventJS;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEvents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;

import java.util.Set;
import java.util.UUID;

public class AftermathEventJSUtil {
    public static void start(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        AftermathEvents.START.post(new AftermathEventJS.StartJS(aftermath, players, level));
    }
    //post ready
    public static void ready(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        AftermathEvents.READY.post(new AftermathEventJS.ReadyJS(aftermath, players, level));
    }
    public static void ongoing(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        AftermathEvents.ONGOING.post(new AftermathEventJS.OngoingJS(aftermath, players, level));
    }
    public static void victory(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        AftermathEvents.VICTORY.post(new AftermathEventJS.VictoryJS(aftermath, players, level));
    }
    public static void celebrating(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        SimpleWeightedRandomList<Item> rewardList = aftermath.getModule().getRewardList();
        AftermathEvents.CELEBRATING.post(new AftermathEventJS.CelebratingJS(aftermath, players, level,rewardList));
    }
    public static void lose(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        AftermathEvents.LOSE.post(new AftermathEventJS.LoseJS(aftermath, players, level));
    }
    public static void end(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        AftermathEvents.END.post(new AftermathEventJS.EndJS(aftermath, players, level));
    }
}
