package com.pancake.surviving_the_aftermath.common.util;

import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.IAftermath;

import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.compat.kubejs.util.AftermathEventJsUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class AftermathEventUtil {
    public static boolean start(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.START);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Start(aftermath, players, level));
        if (isKubejs()) {
            boolean postJS = AftermathEventJsUtil.start(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean ready(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.READY);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Ready(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJsUtil.ready(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean ongoing(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.ONGOING);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Ongoing(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJsUtil.ongoing(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean victory(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.VICTORY);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Victory(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJsUtil.victory(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean celebrating(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.CELEBRATING);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Celebrating(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJsUtil.celebrating(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean lose(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.LOSE);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.Lose(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJsUtil.lose(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }
    public static boolean end(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        aftermath.setState(AftermathState.END);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(new AftermathEvent.End(aftermath, players, level));
        if (isKubejs()){
            boolean postJS = AftermathEventJsUtil.end(aftermath, players, level);
            return postForge && postJS;
        }
        return postForge;
    }

    public static boolean modify(ResourceLocation location, Collection<IAftermathModule> aftermathModules) {
        if (isKubejs()){
            return AftermathEventJsUtil.modify(location, aftermathModules);
        }
        return true;
    }

    private static boolean isKubejs() {
        return ModList.get().isLoaded("kubejs");
    }

}
