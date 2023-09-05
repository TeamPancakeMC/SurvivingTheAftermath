package mod.surviving_the_aftermath.raid;

import mod.surviving_the_aftermath.data.*;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public class RaidFactory {

    public static IRaid create(ServerLevel level, CompoundTag nbt) {
        String identifier = nbt.getString("identifier");
        IRaid raid = null;
        switch (identifier) {
            case "nether_raid" -> raid = new NetherRaid(level, nbt);
        };
        if (raid != null) {
            raid.create(level);
        }
        return raid;
    }

    public static ModDifficultyLoader getDifficulty(int difficulty) {
        return switch (difficulty) {
            case 0 -> Peaceful.getInstance();
            case 1 -> Easy.getInstance();
            case 2 -> Normal.getInstance();
            case 3 -> Hard.getInstance();
            default -> null;
        };
    }
}
