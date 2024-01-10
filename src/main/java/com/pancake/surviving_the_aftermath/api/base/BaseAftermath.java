package com.pancake.surviving_the_aftermath.api.base;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathManager;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.data.pack.AftermathModuleLoader;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import org.slf4j.Logger;

import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class BaseAftermath<T extends IAftermathModule> implements IAftermath<IAftermathModule> {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected final AftermathManager MANAGER = AftermathManager.getInstance();
    protected AftermathState state;
    protected ServerLevel level;
    protected Set<UUID> players = Sets.newLinkedHashSet();
    protected Set<UUID> enemies = Sets.newLinkedHashSet();
    protected T module;
    protected final ServerBossEvent progress = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    protected final UUID uuid = progress.getId();
    protected float progressPercent = progress.getProgress();

    public BaseAftermath(float progressPercent, T module, Set<UUID> players, Set<UUID> enemies) {
        this.progressPercent = progressPercent;
        this.module = module;
        this.players = players;
        this.enemies = enemies;
    }

    public BaseAftermath(ServerLevel level) {
        this.level = level;
        this.module = (T) getRandomAftermathModule();
    }

    public BaseAftermath() {
    }

    public IAftermathModule getRandomAftermathModule() {
        Collection<IAftermathModule> modules = AftermathModuleLoader.AFTERMATH_MODULE_MAP.get(getRegistryName());
        return modules.stream().findAny().get();
    }

    @Override
    public void tick() {
        if (isEnd()) return;

        updateProgress();
        if (state == AftermathState.VICTORY){
            this.progressPercent = 0;
            spawnRewards();
        }
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
        this.progress.removeAllPlayers();
    }

    @Override
    public void lose() {
        this.progress.removeAllPlayers();
    }
    public void setState(AftermathState aftermathState) {
        this.state = aftermathState;
    }
    @Override
    public void spawnRewards() {

    }

    @Override
    public void ready() {

    }

    public AftermathState getState() {
        return state;
    }

    public ServerLevel getLevel() {
        return level;
    }

    @Override
    public Set<UUID> getPlayers() {
        return players;
    }

    @Override
    public Set<UUID> getEnemies() {
        return enemies;
    }

    @Override
    public T getModule() {
        return module;
    }

    public ServerBossEvent getProgress() {
        return progress;
    }

    public UUID getUuid() {
        return uuid;
    }

    public float getProgressPercent() {
        return progressPercent;
    }
}
