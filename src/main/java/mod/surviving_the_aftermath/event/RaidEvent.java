package mod.surviving_the_aftermath.event;

import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.eventbus.api.Event;

import java.util.List;
import java.util.UUID;

public class RaidEvent extends Event {
    private List<UUID> players;
    private ServerLevel level;

    public List<UUID> getPlayers() {
        return players;
    }

    public ServerLevel getLevel() {
        return level;
    }

    public RaidEvent(List<UUID> players, ServerLevel level) {
        this.players = players;
        this.level = level;
    }

    public static class Start extends RaidEvent {
        public Start(List<UUID> players, ServerLevel level) {
            super(players, level);
        }
    }
    public static class End extends RaidEvent {
        public End(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
    public static class Ongoing extends RaidEvent {
        public Ongoing(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
    public static class Victory extends RaidEvent {
        public Victory(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
    public static class Lose extends RaidEvent {
        public Lose(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
    public static class Celebrating extends RaidEvent {
        public Celebrating(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
    public static class CelebrateEnd extends RaidEvent {
        public CelebrateEnd(List<UUID> players, ServerLevel level) {
            super(players,level);
        }
    }
}
