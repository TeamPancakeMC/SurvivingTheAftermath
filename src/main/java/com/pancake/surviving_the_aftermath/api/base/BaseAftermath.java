package com.pancake.surviving_the_aftermath.api.base;

import com.google.common.collect.Sets;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.data.pack.AftermathModuleLoader;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.util.AftermathEventUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

public abstract class BaseAftermath implements IAftermath {
    public ServerLevel level;
    public IAftermathModule module;
    public AftermathState state;

    protected Set<UUID> players = Sets.newHashSet();

    protected ServerBossEvent progress = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    protected UUID uuid = progress.getId();
    protected float progressPercent = progress.getProgress();


    public BaseAftermath(AftermathState state,IAftermathModule module, Set<UUID> players, float progressPercent) {
        this.state = state;
        this.module = module;
        this.players = players;
        this.progressPercent = progressPercent;
    }

    public BaseAftermath(ServerLevel level) {
        this.level = level;
        this.module = getRandomAftermathModule();
        AftermathEventUtil.start(this,players,level);
    }
    public BaseAftermath(BaseRaidModule module, ServerLevel level) {
        this.level = level;
        this.module = module;
        AftermathEventUtil.start(this,players,level);
    }


    public BaseAftermath() {
    }

    protected abstract void init();

    @Override
    public void tick() {
        if (isEnd()) return;
        updateProgress();
        updatePlayers();

        if (state == AftermathState.VICTORY){
            this.progressPercent = 0;
            AftermathEventUtil.celebrating(this,players,level);
        }
    }

    @Override
    public void updateProgress() {
        progress.setProgress(progressPercent);
    }

    public void updatePlayers() {
        final Set<ServerPlayer> oldPlayers = Sets.newHashSet(progress.getPlayers());
        List<ServerPlayer> players1 = level.getPlayers(this.validPlayer());
        final Set<ServerPlayer> newPlayers = Sets.newHashSet(players1);
        players.clear();
        newPlayers.stream()
                .filter(player -> !oldPlayers.contains(player))
                .forEach(progress::addPlayer);
        oldPlayers.stream()
                .filter(player -> !newPlayers.contains(player))
                .forEach(progress::removePlayer);
        progress.getPlayers().forEach(player -> players.add(player.getUUID()));
    }

    public Predicate<? super ServerPlayer> validPlayer() {
        return (Predicate<ServerPlayer>) player -> !player.isSpectator();
    }

    @Override
    public boolean isCreate(Level level, BlockPos pos, @Nullable Player player) {
        return module.isCreate(level, pos, player);
    }

    @Override
    public void createRewards() {

    }
    @Override
    public IAftermathModule getModule() {
        return module;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public float getProgressPercent() {
        return progressPercent;
    }

    @Override
    public AftermathState getState() {
        return state;
    }

    @Override
    public void setLevel(ServerLevel level) {
        this.level = level;
    }

    @Override
    public void setState(AftermathState state) {
        this.state = state;
    }

    @Override
    public boolean isEnd() {
        return state == AftermathState.END;
    }
    public IAftermathModule getRandomAftermathModule() {
        Collection<IAftermathModule> modules = AftermathModuleLoader.AFTERMATH_MODULE_MAP.get(getRegistryName());
        Optional<IAftermathModule> any = modules.stream().findAny();
        return any.orElse(null);
    }

    public void end(){
        AftermathEventUtil.end(this,players,level);
        progress.removeAllPlayers();
    }
    public void lose(){
        AftermathEventUtil.lose(this,players,level);
        progress.removeAllPlayers();
    }
}