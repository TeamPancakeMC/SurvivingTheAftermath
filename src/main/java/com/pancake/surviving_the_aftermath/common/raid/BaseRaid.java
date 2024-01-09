package com.pancake.surviving_the_aftermath.common.raid;


import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.tracker.MobBattleTracker;
import com.pancake.surviving_the_aftermath.common.tracker.RaidMobBattleTracker;
import com.pancake.surviving_the_aftermath.common.tracker.RaidPlayerBattleTracker;
import com.pancake.surviving_the_aftermath.common.util.RandomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;
import org.slf4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public abstract class BaseRaid<T extends BaseRaidModule> extends BaseAftermath<BaseRaidModule> implements IRaid {
    public static final Logger LOGGER = LogUtils.getLogger();
    protected int currentWave = -1;
    protected int totalEnemy = 0;
    protected BlockPos centerPos;
    public BaseRaid(float progressPercent, BaseRaidModule module, Set<UUID> players, Set<UUID> enemies, Set<ITracker> trackers, BlockPos centerPos, int currentWave, int totalEnemy) {
        super(progressPercent, module, players, enemies,trackers);
        this.centerPos = centerPos;
        this.currentWave = currentWave;
        this.totalEnemy = totalEnemy;
    }
    public BaseRaid(ServerLevel level, BlockPos centerPos) {
        super(level);
        this.centerPos = centerPos;
    }

    public BaseRaid() {
    }

    @Override
    public void bindTrackers() {
//        API.getTracker(uuid, MobBattleTracker.IDENTIFIER)
//                .forEach(this::addTracker);
    }

    @Override
    public boolean isCreate() {
        Map<UUID, IAftermath<IAftermathModule>> aftermathMap = MANAGER.getAftermathMap();
        return aftermathMap.values().stream()
                .filter(aftermath -> aftermath instanceof IRaid)
                .map(aftermath -> (IRaid) aftermath)
                .noneMatch(raid -> raid.getCenterPos().distSqr(centerPos) < Math.pow(raid.getRadius(), 2));

    }

    @Override
    public int getRadius() {
        return 50;
    }
    protected abstract List<LazyOptional<Entity>> spawnEntities(IEntityInfoModule module);


    public Player randomPlayersUnderAttack(){
        return level.getPlayerByUUID(RandomUtils.getRandomElement(players));
    }


    public boolean join(Entity entity) {
        if (state == AftermathState.ONGOING &&
                Math.sqrt(entity.blockPosition().distSqr(centerPos)) < getRadius() &&
                enemies.add(entity.getUUID())) {
            totalEnemy++;
            return true;
        }
        return false;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getTotalEnemy() {
        return totalEnemy;
    }

    @Override
    public BlockPos getCenterPos() {
        return centerPos;
    }
}
