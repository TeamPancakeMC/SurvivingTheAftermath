package com.pancake.surviving_the_aftermath.common.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.Cancelable;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public class AftermathEvent extends Event {
    private List<UUID> players;
    private ServerLevel level;

    public List<UUID> getPlayers() {
        return players;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public AftermathEvent(List<UUID> players, ServerLevel level) {
        this.players = players;
        this.level = level;
    }

    public static class Start extends AftermathEvent {
        public Start(List<UUID> players, ServerLevel level) {
            super(players, level);
        }
    }
    public static class End extends AftermathEvent {
        public End(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
    public static class Ready extends AftermathEvent {
        public Ready(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }

    public static class Ongoing extends AftermathEvent {
        public Ongoing(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
    public static class Victory extends AftermathEvent {
        public Victory(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
    public static class Lose extends AftermathEvent {
        public Lose(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
    @Cancelable
    public static class Celebrating extends AftermathEvent {
        private SimpleWeightedRandomList<Item> rewardList;
        public Celebrating(List<UUID> players, SimpleWeightedRandomList<Item> rewardList, ServerLevel level) {
            super(players,level);
        }

        public SimpleWeightedRandomList<Item> getRewardList() {
            return rewardList;
        }

        public void setRewardList(SimpleWeightedRandomList<Item> rewardList) {
            this.rewardList = rewardList;
        }
    }
}
