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
import java.util.function.Supplier;

public abstract class BaseAftermath<T extends BaseAftermathModule> implements IAftermath<BaseAftermathModule> {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected final AftermathAPI AFTERMATH_API = AftermathAPI.getInstance();
    protected final AftermathManager AFTERMATH_MANAGER = AftermathManager.getInstance();
    protected final List<ITracker> TRACKERS = Lists.newArrayList();
    protected AftermathState state;
    protected ServerLevel level;
    protected Set<UUID> players = Sets.newHashSet();
    protected Set<UUID> enemies = Sets.newHashSet();
    protected T module;
    private final String NAME = Constant.MOD_NAME + "." + getUniqueIdentifier();
    protected final ServerBossEvent progress = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    protected final UUID uuid = progress.getId();
    protected float progressPercent = progress.getProgress();
    public BaseAftermath(ServerLevel level) {
        this.level = level;
        this.module = (T) AFTERMATH_API.getRandomAftermathModule(getUniqueIdentifier())
                .orElseGet(() -> AFTERMATH_API.getAftermathMap().get(getUniqueIdentifier()).get(0));
        bindTrackers();
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
        TRACKERS.forEach(tracker -> tracker.setUUID(uuid));
        return TRACKERS;
    }

    @Override
    public boolean isEnd() {
        return this.state == AftermathState.END ;
    }

    @Override
    public boolean isLose() {
        return this.state == AftermathState.LOSE;
    }

    @Override
    public UUID getUUID() {
        return this.uuid;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(Constant.IDENTIFIER, this.getUniqueIdentifier());
        compoundTag.put(Constant.MODULE, module.serializeNBT());
        compoundTag.putFloat(Constant.PROGRESS, progressPercent);

        ListTag tags = new ListTag();
        this.getTrackers().forEach(tracker -> {
            System.out.println("tracker :" + tracker);
            tags.add(tracker.serializeNBT());
        });
        compoundTag.put(Constant.TRACKER, tags);

        ListTag playerTags = new ListTag();
        players.forEach(uuid -> playerTags.add(NbtUtils.createUUID(uuid)));
        compoundTag.put(Constant.PLAYERS, playerTags);

        ListTag enemiesTags = new ListTag();
        enemies.forEach(uuid -> enemiesTags.add(NbtUtils.createUUID(uuid)));
        compoundTag.put(Constant.ENEMIES, enemiesTags);

        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.progressPercent = nbt.getFloat(Constant.PROGRESS);

        CompoundTag moduleTag = nbt.getCompound(Constant.MODULE);
        IAftermathModule aftermathModule = AFTERMATH_API.getAftermathModule(this.getUniqueIdentifier());
        aftermathModule.deserializeNBT(moduleTag);
        this.module = (T) aftermathModule;

        ListTag tags = nbt.getList(Constant.TRACKER, Tag.TAG_COMPOUND);
        tags.forEach(tag -> {
            CompoundTag trackerTag = (CompoundTag) tag;
            TRACKERS.stream()
                    .filter(tracker -> tracker.getUniqueIdentifier().equals(trackerTag.getString(Constant.IDENTIFIER)))
                    .map(tracker -> (ITracker) tracker)
                    .forEach(tracker -> tracker.deserializeNBT(trackerTag));
        });


        ListTag playerTags = nbt.getList(Constant.PLAYERS, Tag.TAG_INT_ARRAY);
        playerTags.forEach(tag -> players.add(NbtUtils.loadUUID(tag)));

        ListTag enemiesTags = nbt.getList(Constant.ENEMIES, Tag.TAG_INT_ARRAY);
        enemiesTags.forEach(tag -> enemies.add(NbtUtils.loadUUID(tag)));
    }

    @Override
    public Predicate<? super ServerPlayer> validPlayer() {
        return (Predicate<ServerPlayer>) player -> !player.isSpectator();
    }


    @Override
    public Set<UUID> getEnemies() {
        return enemies;
    }

    @Override
    public Set<UUID> getPlayers() {
        return players;
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

    @Override
    public void lose() {
        AftermathEventUtil.lose(this, players, level);
        this.progress.removeAllPlayers();
    }

    public void setState(AftermathState aftermathState) {
        this.state = aftermathState;
    }

    public AftermathState getState() {
        return state;
    }
}
