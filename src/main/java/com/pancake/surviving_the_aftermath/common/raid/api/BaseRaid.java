package com.pancake.surviving_the_aftermath.common.raid.api;


import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
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
import java.util.UUID;

public abstract class BaseRaid<T extends BaseRaidModule> extends BaseAftermath<BaseRaidModule> implements IRaid{
    public static final Logger LOGGER = LogUtils.getLogger();
    protected int currentWave = -1;
    protected BlockPos centerPos;
    public BaseRaid(ServerLevel level, BlockPos centerPos) {
        super(level);
        this.centerPos = centerPos;
    }

    public BaseRaid(ServerLevel level) {
        super(level);
    }

    @Override
    public void bindTrackers() {
        API.getTracker(uuid, MobBattleTracker.IDENTIFIER)
                .forEach(this::addTracker);
    }

    @Override
    public boolean isCreate() {
        Map<UUID, IAftermath<BaseAftermathModule>> aftermathMap = MANAGER.getAftermathMap();
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
        compoundTag.put(Constant.CENTER_POS, NbtUtils.writeBlockPos(centerPos));
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.currentWave = nbt.getInt(Constant.CURRENT_WAVE);
        this.centerPos = NbtUtils.readBlockPos(nbt.getCompound(Constant.CENTER_POS));
    }


    public Player randomPlayersUnderAttack(){
        return level.getPlayerByUUID(RandomUtils.getRandomElement(players));
    }


}
