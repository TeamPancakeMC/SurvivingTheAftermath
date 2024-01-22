package com.pancake.surviving_the_aftermath.compat.kubejs.event;

import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermathEvent;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.Event;

import java.util.Set;
import java.util.UUID;

public class AftermathEventJS extends EventJS implements IAftermathEvent {
    private final IAftermath aftermath;
    private final Set<UUID> players;
    private final ServerLevel level;
    private final IAftermathModule module;

    public Set<UUID> getPlayers() {
        return players;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public IAftermath getAftermath() {
        return aftermath;
    }
    public IAftermathModule getModule(){return module;}

    public AftermathEventJS(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
        this.aftermath = aftermath;
        this.players = players;
        this.level = level;
        this.module = aftermath.getModule();
    }

    @Override
    public Event getForge() {
        return null;
    }

    @Override
    public EventJS getKubeJS() {
        return this;
    }

    public static class StartJS extends AftermathEventJS {
        public StartJS(IAftermath aftermath,Set<UUID> players, ServerLevel level) {
            super(aftermath,players, level);
        }
    }
    public static class EndJS extends AftermathEventJS {

        public EndJS(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class ReadyJS extends AftermathEventJS {

        public ReadyJS(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }

    public static class OngoingJS extends AftermathEventJS {

        public OngoingJS(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class VictoryJS extends AftermathEventJS {

        public VictoryJS(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class LoseJS extends AftermathEventJS {

        public LoseJS(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }

    public static class CelebratingJS extends AftermathEventJS {
        public CelebratingJS(IAftermath aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
}
