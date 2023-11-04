package com.pancake.surviving_the_aftermath.api.base;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathManager;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.util.AftermathEventUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.slf4j.Logger;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class BaseAftermath<T extends BaseAftermathModule> implements IAftermath<BaseAftermathModule> {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected final AftermathAPI AFTERMATH_API = AftermathAPI.getInstance();
    protected final AftermathManager AFTERMATH_MANAGER = AftermathManager.getInstance();
    protected final List<ITracker> TRACKERS = Lists.newArrayList();
    protected AftermathState state;
    protected UUID uuid;
    protected ServerLevel level;
    protected List<UUID> players = Lists.newArrayList();
    protected T module;
    private final String NAME = Constant.MOD_NAME + "." + getUniqueIdentifier();
    protected static final ServerBossEvent progress = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    protected float progressPercent = progress.getProgress();
    public BaseAftermath(ServerLevel level) {
        this.level = level;
        this.uuid = progress.getId();
        this.module = (T) AFTERMATH_API.getRandomAftermathModule(getUniqueIdentifier())
                .orElseGet(() -> AFTERMATH_API.getAftermathMap().get(getUniqueIdentifier()).get(0));
    }

    @Override
    public void tick() {
        if (isEnd()) return;

        updateProgress();
        if (state == AftermathState.VICTORY){
            spawnRewards();
        }

//        long gameTime = level.getGameTime();
//        if (gameTime % 20 == 0) {
//
//
//        }
    }

    protected void addTracker(ITracker tracker) {
        TRACKERS.add(tracker);
    }

    @Override
    public List<ITracker> getTrackers() {
        return TRACKERS;
    }

    @Override
    public boolean isEnd() {
        return this.state == AftermathState.END;
    }
    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(Constant.IDENTIFIER, this.getUniqueIdentifier());
        compoundTag.putUUID(Constant.UUID, this.uuid);
        compoundTag.put(Constant.MODULE, module.serializeNBT());
        compoundTag.putFloat(Constant.PROGRESS, progressPercent);

        ListTag tags = new ListTag();
        players.forEach(uuid -> tags.add(NbtUtils.createUUID(uuid)));
        compoundTag.put(Constant.PLAYERS, tags);

        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.uuid = nbt.getUUID(Constant.UUID);
        this.progressPercent = nbt.getFloat(Constant.PROGRESS);

        CompoundTag moduleTag = nbt.getCompound(Constant.MODULE);
        IAftermathModule aftermathModule = AFTERMATH_API.getAftermathModule(this.getUniqueIdentifier());
        aftermathModule.deserializeNBT(moduleTag);
        this.module = (T) aftermathModule;


        ListTag tags = nbt.getList(Constant.PLAYERS, Tag.TAG_INT_ARRAY);
        tags.forEach(tag -> players.add(NbtUtils.loadUUID(tag)));
    }

    @Override
    public Predicate<? super ServerPlayer> validPlayer() {
        return (Predicate<ServerPlayer>) player -> !player.isSpectator();
    }

    @Override
    public void updatePlayers() {
        final Set<ServerPlayer> oldPlayers = Sets.newHashSet(progress.getPlayers());
        final Set<ServerPlayer> newPlayers = Sets.newHashSet(level.getPlayers(this.validPlayer()));
        players.clear();
        newPlayers.stream()
                .filter(player -> !oldPlayers.contains(player))
                .forEach(progress::addPlayer);
        oldPlayers.stream()
                .filter(player -> !newPlayers.contains(player))
                .forEach(progress::removePlayer);
        progress.getPlayers().forEach(player -> players.add(player.getUUID()));
    }

    @Override
    public void updateProgress() {
        progress.setProgress(progressPercent);
        updatePlayers();
    }

    @Override
    public void end() {
        AftermathEventUtil.end(this, players, level);
        this.progress.removeAllPlayers();
    }

    public void setState(AftermathState aftermathState) {
        this.state = aftermathState;
    }

    public AftermathState getState() {
        return state;
    }
}
