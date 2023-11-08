package com.pancake.surviving_the_aftermath.common.event;

import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AftermathEvent extends Event {
    private final BaseAftermath<BaseAftermathModule> aftermath;
    private final Set<UUID> players;
    private final ServerLevel level;

    public Set<UUID> getPlayers() {
        return players;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public BaseAftermath<BaseAftermathModule> getAftermath() {
        return aftermath;
    }

    public AftermathEvent(BaseAftermath<BaseAftermathModule> aftermath,Set<UUID> players, ServerLevel level) {
        this.aftermath = aftermath;
        this.players = players;
        this.level = level;
    }

    public static class Start extends AftermathEvent {
        public Start(BaseAftermath<BaseAftermathModule> aftermath,Set<UUID> players, ServerLevel level) {
            super(aftermath,players, level);
        }
    }
    public static class End extends AftermathEvent {

        public End(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class Ready extends AftermathEvent {

        public Ready(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }

    public static class Ongoing extends AftermathEvent {

        public Ongoing(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class Victory extends AftermathEvent {

        public Victory(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    public static class Lose extends AftermathEvent {

        public Lose(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level) {
            super(aftermath, players, level);
        }
    }
    @Cancelable
    public static class Celebrating extends AftermathEvent {
        private SimpleWeightedRandomList<Item> rewardList;

        public Celebrating(BaseAftermath<BaseAftermathModule> aftermath, Set<UUID> players, ServerLevel level,SimpleWeightedRandomList<Item> rewardList) {
            super(aftermath, players, level);
            this.rewardList = rewardList;
        }
        public SimpleWeightedRandomList<Item> getRewardList() {
            return rewardList;
        }

        public void setRewardList(SimpleWeightedRandomList<Item> rewardList) {
            this.rewardList = rewardList;
        }
    }
}
