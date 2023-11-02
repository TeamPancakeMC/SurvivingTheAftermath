package com.pancake.surviving_the_aftermath.common.raid.api;


import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.tracker.PlayerBattleTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.Map;
import java.util.UUID;

public abstract class BaseRaid<T extends BaseRaidModule> extends BaseAftermath<BaseRaidModule> implements IRaid{
    protected int currentWave = -1;
    protected BlockPos centerPos;
    protected final List<UUID> enemies = Lists.newArrayList();
    public BaseRaid(ServerLevel level, BlockPos centerPos) {
        super(level);
        this.centerPos = centerPos;
    }

    public BaseRaid(ServerLevel level, CompoundTag compoundTag) {
        super(level, compoundTag);
    }

    @Override
    public void bindTrackers() {
        addTracker(new PlayerBattleTracker());
    }

    @Override
    public boolean isCreate() {
        Map<UUID, IAftermath<BaseAftermathModule>> aftermathMap = AFTERMATH_MANAGER.getAftermathMap();
        return aftermathMap.values().stream()
                .filter(aftermath -> aftermath instanceof IRaid)
                .map(aftermath -> (IRaid) aftermath)
                .noneMatch(raid -> raid.getCenterPos().distSqr(centerPos) < Math.pow(raid.getRadius(), 2));

    }

    @Override
    public BlockPos getCenterPos() {
        return centerPos;
    }

    @Override
    public int getRadius() {
        return 50;
    }
    protected abstract List<LazyOptional<Entity>> spawnEntities(IEntityInfoModule module);

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        compoundTag.putInt(Constant.CURRENT_WAVE, currentWave);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.currentWave = nbt.getInt(Constant.CURRENT_WAVE);
    }


    public Player randomPlayersUnderAttack(){
        return level.getPlayerByUUID(players.get(level.random.nextInt(players.size())));
    }


}
