package com.pancake.surviving_the_aftermath.api.base;

import com.google.common.collect.Sets;
import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.data.pack.AftermathModuleLoader;
import com.pancake.surviving_the_aftermath.common.module.condition.StructureConditionModule;
import com.pancake.surviving_the_aftermath.util.StructureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public abstract class BaseAftermath<T extends IAftermathModule> implements IAftermath<IAftermathModule> {
    protected final AftermathManager MANAGER = AftermathManager.getInstance();
    protected AftermathState state;
    protected Level level;
    protected Set<UUID> players = Sets.newHashSet();
    protected Set<UUID> enemies = Sets.newHashSet();
    protected T module;
    protected BlockPos centerPos;
    protected Set<BlockPos> spawnPos = Sets.newHashSet();
    protected final ServerBossEvent progress = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    protected final UUID uuid = progress.getId();
    protected float progressPercent = progress.getProgress();
    protected int rewardTime;


    public BaseAftermath(Set<UUID> players, Set<UUID> enemies, Set<BlockPos> spawnPos,T module, BlockPos centerPos, float progressPercent, int rewardTime) {
        this.players = players;
        this.enemies = enemies;
        this.spawnPos = spawnPos;
        this.module = module;
        this.centerPos = centerPos;
        this.progressPercent = progressPercent;
        this.rewardTime = rewardTime;
    }

    public BaseAftermath(Level level, BlockPos pos){
        this.level = level;
        this.module = (T) getRandomAftermathModule();
        SetSpawnPos(level,pos);
    }


    public BaseAftermath() {
    }

    public IAftermathModule getRandomAftermathModule() {
        Collection<IAftermathModule> modules = AftermathModuleLoader.AFTERMATH_MODULE_MAP.get(getRegistryName());
        return modules.stream().findAny().get();
    }

    public void SetSpawnPos(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            Optional<StructureConditionModule> module = getModule().getConditions().stream()
                    .filter(condition -> condition instanceof StructureConditionModule)
                    .map(condition -> (StructureConditionModule) condition)
                    .findFirst();
            if (module.isPresent()){
                StructureUtils.handleDataMarker(serverLevel, pos, module.get().getResourceLocation(), (serverLevel1,metadata,startPos,metaPos) -> {
                    centerPos = startPos;
                    setMobSpawnPos(serverLevel1,metadata,startPos,metaPos);
                });
            }else {
                centerPos = pos;
                spawnPos.add(pos);
            }
        }
    }

    public void setMobSpawnPos(ServerLevel serverLevel, String metadata, BlockPos startPos, BlockPos metaPos) {
        spawnPos.add(metaPos);
    }


    @Override
    public boolean isCreate(Level level, BlockPos pos, @Nullable Player player) {
        return getModule().isCreate(level, pos,player);
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
    public void updateProgress() {
        progress.setProgress(progressPercent);
        updatePlayers();
    }

    @Override
    public void updatePlayers() {
        if (level instanceof ServerLevel serverLevel){
            final Set<ServerPlayer> oldPlayers = Sets.newHashSet(progress.getPlayers());
            final Set<ServerPlayer> newPlayers = Sets.newHashSet(serverLevel.getPlayers(this.validPlayer()));
            players.clear();
            newPlayers.stream()
                    .filter(player -> !oldPlayers.contains(player))
                    .forEach(progress::addPlayer);
            oldPlayers.stream()
                    .filter(player -> !newPlayers.contains(player))
                    .forEach(progress::removePlayer);
            progress.getPlayers().forEach(player -> players.add(player.getUUID()));
        }
    }

    @Override
    public Predicate<? super ServerPlayer> validPlayer() {
        return (Predicate<ServerPlayer>) player -> !player.isSpectator();
    }
    @Override
    public void spawnRewards() {

    }
    @Override
    public void end() {
        this.progress.removeAllPlayers();
    }

    @Override
    public void lose() {
        this.progress.removeAllPlayers();
    }

    @Override
    public boolean isEnd() {
        return this.state == AftermathState.END;
    }
    @Override
    public boolean isLose() {
        return this.state == AftermathState.LOSE;
    }
    @Override
    public AftermathState getState() {
        return state;
    }
    @Override
    public Level getLevel() {
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
    public IAftermathModule getModule() {
        return module;
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }
    @Override
    public float getProgressPercent() {
        return progressPercent;
    }

    public BlockPos getCenterPos() {
        return centerPos;
    }

    public Set<BlockPos> getSpawnPos() {
        return spawnPos;
    }

    public int getRewardTime() {
        return rewardTime;
    }

    @Override
    public void setLevel(Level level) {
        this.level = level;
    }
}