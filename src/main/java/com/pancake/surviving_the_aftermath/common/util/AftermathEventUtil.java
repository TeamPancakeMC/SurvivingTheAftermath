package com.pancake.surviving_the_aftermath.common.util;

import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AftermathEventUtil {
    //post start
    public static void start(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.START);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Start(aftermath,players, level));
    }
    //post ready
    public static void ready(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.READY);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Ready(aftermath,players, level));
    }
    //post ongoing
    public static void ongoing(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.ONGOING);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Ongoing(aftermath,players, level));
    }

    //post victory
    public static void victory(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.VICTORY);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Victory(aftermath,players, level));
    }
    //post celebrating
    public static void celebrating(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.CELEBRATING);
        SimpleWeightedRandomList<Item> rewardList = aftermath.getModule().getRewardList();
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Celebrating(aftermath,players, level,rewardList));
    }
    //post lose
    public static void lose(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.LOSE);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Lose(aftermath,players, level));
    }
    //post end
    public static void end(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.END);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.End(aftermath,players, level));
    }
}
