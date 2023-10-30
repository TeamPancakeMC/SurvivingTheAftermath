package com.pancake.surviving_the_aftermath.common.raid.api;


import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.common.tracker.PlayerBattleTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.UUID;

public abstract class BaseRaid extends BaseAftermath implements IRaid{
    //中心点
    protected BlockPos centerPos;
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
        Map<UUID, IAftermath> aftermathMap = AFTERMATH_MANAGER.getAftermathMap();
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
}
