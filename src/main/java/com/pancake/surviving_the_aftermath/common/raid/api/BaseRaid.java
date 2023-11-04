package com.pancake.surviving_the_aftermath.common.raid.api;


import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.tracker.PlayerBattleTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
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
    protected List<UUID> enemies = Lists.newArrayList();
    public BaseRaid(ServerLevel level, BlockPos centerPos) {
        super(level);
        this.centerPos = centerPos;
    }

    public BaseRaid(ServerLevel level) {
        super(level);
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
        compoundTag.put(Constant.CENTER_POS, NbtUtils.writeBlockPos(centerPos));


        ListTag tags = new ListTag();
        enemies.forEach(uuid -> tags.add(NbtUtils.createUUID(uuid)));
        compoundTag.put(Constant.ENEMIES, tags);

        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        this.currentWave = nbt.getInt(Constant.CURRENT_WAVE);
        this.centerPos = NbtUtils.readBlockPos(nbt.getCompound(Constant.CENTER_POS));


        ListTag tags = nbt.getList(Constant.ENEMIES, Tag.TAG_INT_ARRAY);
        tags.forEach(tag -> enemies.add(NbtUtils.loadUUID(tag)));
    }


    public Player randomPlayersUnderAttack(){
        return level.getPlayerByUUID(players.get(level.random.nextInt(players.size())));
    }


}
