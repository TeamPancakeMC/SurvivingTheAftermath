package com.pancake.surviving_the_aftermath.common.util;

import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermathEvent;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.compat.kubejs.util.AftermathEventJsUtil;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.ModList;

import java.util.Set;
import java.util.UUID;

public class AftermathEventUtil {
    private static boolean executeEvent(IAftermath aftermath, Set<UUID> players, ServerLevel level, AftermathState state, AftermathEventExecutor executor) {
        aftermath.setState(state);
        boolean postForge = !MinecraftForge.EVENT_BUS.post(executor.execute(aftermath, players, level).getForge());
        if (isKubejs()) {
            boolean postJS = AftermathEventJsUtil.execute(executor.execute(aftermath, players, level).getKubeJS());
            return postForge && postJS;
        }
        return postForge;
    }

    public static boolean start(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return executeEvent(aftermath, players, level, AftermathState.START, AftermathEvent.Start::new);
    }
    public static boolean end(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return executeEvent(aftermath, players, level, AftermathState.END, AftermathEvent.End::new);
    }
    public static boolean ready(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return executeEvent(aftermath, players, level, AftermathState.READY, AftermathEvent.Ready::new);
    }
    public static boolean victory(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return executeEvent(aftermath, players, level, AftermathState.VICTORY, AftermathEvent.Victory::new);
    }

    public static boolean celebrating(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return executeEvent(aftermath, players, level, AftermathState.CELEBRATING, AftermathEvent.Celebrating::new);
    }

    public static boolean lose(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return executeEvent(aftermath, players, level, AftermathState.LOSE, AftermathEvent.Lose::new);
    }
    public static boolean ongoing(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        return executeEvent(aftermath, players, level, AftermathState.ONGOING, AftermathEvent.Ongoing::new);
    }



    private static boolean isKubejs() {
        return ModList.get().isLoaded("kubejs");
    }

    @FunctionalInterface
    interface AftermathEventExecutor {
        IAftermathEvent execute(IAftermath aftermath, Set<UUID> players, ServerLevel level);
    }
}

