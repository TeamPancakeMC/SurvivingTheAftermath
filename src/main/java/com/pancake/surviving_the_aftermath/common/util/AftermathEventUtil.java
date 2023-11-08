package com.pancake.surviving_the_aftermath.common.util;

import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.compat.kubejs.util.AftermathEventJSUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import java.util.Set;
import java.util.UUID;

public class AftermathEventUtil {
    public static void start(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.START);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Start(aftermath,players, level));
        if (ModList.get().isLoaded("kubejs")){
            AftermathEventJSUtil.start(aftermath,players, level);
        }
    }
    public static void ready(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.READY);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Ready(aftermath,players, level));
        if (ModList.get().isLoaded("kubejs")){
            AftermathEventJSUtil.ready(aftermath,players, level);
        }
    }
    public static void ongoing(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.ONGOING);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Ongoing(aftermath,players, level));
        if (ModList.get().isLoaded("kubejs")){
            AftermathEventJSUtil.ongoing(aftermath,players, level);
        }
    }
    public static void victory(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.VICTORY);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Victory(aftermath,players, level));
        if (ModList.get().isLoaded("kubejs")){
            AftermathEventJSUtil.victory(aftermath,players, level);
        }
    }
    public static void celebrating(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.CELEBRATING);
        SimpleWeightedRandomList<Item> rewardList = aftermath.getModule().getRewardList();
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Celebrating(aftermath,players, level,rewardList));
        if (ModList.get().isLoaded("kubejs")){
            AftermathEventJSUtil.celebrating(aftermath,players, level);
        }
    }
    public static void lose(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.LOSE);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Lose(aftermath,players, level));
        if (ModList.get().isLoaded("kubejs")){
            AftermathEventJSUtil.lose(aftermath,players, level);
        }
    }
    public static void end(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.END);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.End(aftermath,players, level));
        if (ModList.get().isLoaded("kubejs")){
            AftermathEventJSUtil.end(aftermath,players, level);
        }
    }

}
