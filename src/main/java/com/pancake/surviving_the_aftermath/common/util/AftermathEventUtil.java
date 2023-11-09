package com.pancake.surviving_the_aftermath.common.util;

import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.compat.kubejs.util.AftermathEventJSUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AftermathEventUtil {
    public static boolean start(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.START);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Start(aftermath, players, level));
        if (isKubejs()) {
            boolean postJS = AftermathEventJSUtil.start(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean ready(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.READY);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Ready(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJSUtil.ready(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean ongoing(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.ONGOING);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Ongoing(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJSUtil.ongoing(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean victory(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.VICTORY);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Victory(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJSUtil.victory(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean celebrating(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.CELEBRATING);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Celebrating(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJSUtil.celebrating(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean lose(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.LOSE);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Lose(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJSUtil.lose(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean end(BaseAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.END);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.End(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJSUtil.end(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }

    public static boolean modify(String identifier, List<IAftermathModule> aftermathModules) {
        if (isKubejs()){
            return AftermathEventJSUtil.modify(identifier, aftermathModules);
        }
        return true;
    }

    private static boolean isKubejs() {
        return ModList.get().isLoaded("kubejs");
    }

}
