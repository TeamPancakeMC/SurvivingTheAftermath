package mod.surviving_the_aftermath.raid;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

import java.util.Locale;

public class Raid {
    private final int id;
    private final ServerLevel level;
    private final ResourceLocation resource;
    private final BlockPos centerPos;
    private RaidStatus status;

    public Raid(int id, ServerLevel level, ResourceLocation res, BlockPos pos) {
        this.id = id;
        this.level = level;
        this.resource = res;
        this.centerPos = pos;
    }
    public void tick() {

    }

    public boolean isOngoing() {
        return this.status == RaidStatus.ONGOING;
    }
    public boolean isCelebrating() {
        return this.status == RaidStatus.CELEBRATING;
    }
    public boolean isCelebratingEnd() {
        return this.status == RaidStatus.CELEBRATE_END;
    }
    public boolean isVictory() {
        return this.status == RaidStatus.VICTORY;
    }
    public boolean isLose() {
        return this.status == RaidStatus.LOSE;
    }
    public boolean isStart() {
        return this.status == RaidStatus.START;
    }
    public boolean isEnd() {
        return this.status == RaidStatus.END;
    }
    public RaidStatus getStatus() {
        return this.status;
    }

    public enum RaidStatus {
        START,
        END,
        ONGOING,
        VICTORY,
        LOSE,
        CELEBRATING,
        CELEBRATE_END;

        private static final RaidStatus[] VALUES = values();

        static RaidStatus getByName(String name) {
            for(RaidStatus raidstatus : VALUES) {
                if (name.equalsIgnoreCase(raidstatus.name())) {
                    return raidstatus;
                }
            }
            return ONGOING;
        }
        public String getName() {
            return this.name().toLowerCase(Locale.ROOT);
        }
    }


}
