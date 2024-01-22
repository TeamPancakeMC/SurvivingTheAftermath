package com.pancake.surviving_the_aftermath.common.event;

import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermathEvent;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.Set;
import java.util.UUID;

public abstract class AftermathEvent extends Event implements IAftermathEvent {
    private final IAftermath aftermath;
    private final IAftermathModule module;
    private final Set<UUID> players;
    private final ServerLevel level;

    public AftermathEvent(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        this.aftermath = aftermath;
        this.module = aftermath.getModule();
        this.players = players;
        this.level = level;
    }

    public IAftermath getAftermath() {
        return aftermath;
    }

    public IAftermathModule getModule() {
        return module;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public ServerLevel getLevel() {
        return level;
    }

    @Override
    public Event getForge() {
        return this;
    }

    @Override
    public EventJS getKubeJS() {
        return null;
    }

    @Cancelable
    public static class Start extends AftermathEvent {
        public Start(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath,players, level);
        }
    }
    public static class End extends AftermathEvent {

        public End(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    @Cancelable
    public static class Ready extends AftermathEvent {

        public Ready(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }

    public static class Ongoing extends AftermathEvent {

        public Ongoing(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class Victory extends AftermathEvent {

        public Victory(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class Lose extends AftermathEvent {

        public Lose(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    @Cancelable
    public static class Celebrating extends AftermathEvent {
        public Celebrating(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }


}
