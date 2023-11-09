package com.pancake.surviving_the_aftermath.compat.kubejs.event;

import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import dev.latvian.mods.kubejs.event.EventJS;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;

import java.util.Set;
import java.util.UUID;
import java.util.function.Supplier;

public class AftermathEventJS extends EventJS {
    private final BaseAftermath<BaseAftermathModule> aftermath;
    private final Set<UUID> players;
    private final ServerLevel level;
    private final BaseAftermathModule module;

    public Set<UUID> getPlayers() {
        return players;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public BaseAftermath<BaseAftermathModule> getAftermath() {
        return aftermath;
    }
    public BaseAftermathModule getModule(){return module;}

    public AftermathEventJS(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
        this.aftermath = aftermath;
        this.players = players;
        this.level = level;
        this.module = aftermath.getModule();
    }

    public static class StartJS extends AftermathEventJS {
        public StartJS(BaseAftermath<BaseAftermathModule> aftermath,Set<UUID> players, ServerLevel level) {
            super(aftermath,players, level);
        }
    }
    public static class EndJS extends AftermathEventJS {

        public EndJS(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class ReadyJS extends AftermathEventJS {

        public ReadyJS(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }

    public static class OngoingJS extends AftermathEventJS {

        public OngoingJS(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class VictoryJS extends AftermathEventJS {

        public VictoryJS(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class LoseJS extends AftermathEventJS {

        public LoseJS(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }

    public static class CelebratingJS extends AftermathEventJS {
        public CelebratingJS(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
}
