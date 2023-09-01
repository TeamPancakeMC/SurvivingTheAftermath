package mod.surviving_the_aftermath.raid;


import mod.surviving_the_aftermath.data.RaidEnemyInfo;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;

import java.util.HashSet;
import java.util.List;
import java.util.UUID;

public interface IRaid {
    default String getIdentifier() {
        return "";
    }

    UUID getUUID();

    boolean create();

    int getRadius();

    BlockPos getCenterPos();

    boolean join(Entity entity);

    int getRewardTime();

    void setSpawnPos(ServerLevel level);

    void updatePlayers(ServerLevel level);

    void updateProgress(ServerLevel level);

    void spawnRewards(ServerLevel level);

    void updateStructure();

    void spawnEnemies(List<RaidEnemyInfo.WaveEntry> entries);

    RaidState getState();

    void setState(RaidState state);

    boolean loseOrEnd();

    void tick(ServerLevel level);

    List<UUID> getPlayers();

    UUID getUuid();

    int getTotalEnemyCount();

    RaidEnemyInfo getRaidEnemyInfo();

    int getWave();

    CompoundTag serializeNBT();

    void deserializeNBT(CompoundTag nbt);

    HashSet<UUID> getEnemies();
}
