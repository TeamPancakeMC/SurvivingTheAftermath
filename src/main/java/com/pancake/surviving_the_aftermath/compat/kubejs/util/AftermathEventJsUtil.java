package com.pancake.surviving_the_aftermath.compat.kubejs.util;

import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEventJS;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathEvents;
import com.pancake.surviving_the_aftermath.compat.kubejs.event.AftermathModifyEventJS;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public class AftermathEventJsUtil {
    public static boolean start(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.START.post(new AftermathEventJS.StartJS(aftermath, players, level)).pass();
    }
    //post ready
    public static boolean ready(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.READY.post(new AftermathEventJS.ReadyJS(aftermath, players, level)).pass();
    }
    public static boolean ongoing(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.ONGOING.post(new AftermathEventJS.OngoingJS(aftermath, players, level)).pass();
    }
    public static boolean victory(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.VICTORY.post(new AftermathEventJS.VictoryJS(aftermath, players, level)).pass();
    }
    public static boolean celebrating(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.CELEBRATING.post(new AftermathEventJS.CelebratingJS(aftermath, players, level)).pass();
    }
    public static boolean lose(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.LOSE.post(new AftermathEventJS.LoseJS(aftermath, players, level)).pass();
    }
    public static boolean end(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return AftermathEvents.END.post(new AftermathEventJS.EndJS(aftermath, players, level)).pass();
    }

    public static boolean modify(ResourceLocation location, Collection<IAftermathModule> aftermathModules) {
        return AftermathEvents.MODIFY.post(new AftermathModifyEventJS(location, aftermathModules)).pass();
    }

    public static boolean execute(EventJS kubeJS) {
        if (kubeJS instanceof AftermathEventJS) {
            AftermathEventJS event = (AftermathEventJS) kubeJS;
            if (event instanceof AftermathEventJS.StartJS) {
                return start(event.getAftermath(), event.getPlayers(), event.getLevel());
            } else if (event instanceof AftermathEventJS.ReadyJS) {
                return ready(event.getAftermath(), event.getPlayers(), event.getLevel());
            } else if (event instanceof AftermathEventJS.OngoingJS) {
                return ongoing(event.getAftermath(), event.getPlayers(), event.getLevel());
            } else if (event instanceof AftermathEventJS.VictoryJS) {
                return victory(event.getAftermath(), event.getPlayers(), event.getLevel());
            } else if (event instanceof AftermathEventJS.CelebratingJS) {
                return celebrating(event.getAftermath(), event.getPlayers(), event.getLevel());
            } else if (event instanceof AftermathEventJS.LoseJS) {
                return lose(event.getAftermath(), event.getPlayers(), event.getLevel());
            } else if (event instanceof AftermathEventJS.EndJS) {
                return end(event.getAftermath(), event.getPlayers(), event.getLevel());
            }
        } else if (kubeJS instanceof AftermathModifyEventJS) {
            AftermathModifyEventJS event = (AftermathModifyEventJS) kubeJS;
            return modify(event.getIdentifier(), event.getAftermathModules());
        }
        return false;
    }
}