package com.pancake.surviving_the_aftermath.api;

import com.google.common.collect.Sets;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.event.AftermathEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.BossEvent;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;
import java.util.function.Predicate;

public abstract class BaseAftermath implements IAftermath{
    protected UUID uuid;
    protected ServerLevel level;
    protected IAftermathModule module;
    protected Set<UUID> players = new HashSet<>();
    protected AftermathState state;
    private String name = SurvivingTheAftermath.MOD_NAME + "." + getIdentifier();
    protected final ServerBossEvent progress = new ServerBossEvent(Component.translatable(name), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
    public BaseAftermath(UUID uuid, ServerLevel serverLevel) {
        this.uuid = uuid;
        this.level = serverLevel;
        this.state = AftermathState.START;
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.Start(players, level));
    }

    public BaseAftermath(CompoundTag compoundTag,ServerLevel level) {
        deserializeNBT(compoundTag);
        this.level = level;
    }

    @Override
    public AftermathState getState() {
        return state;
    }

    @Override
    public void setState(AftermathState state) {
        this.state = state;
    }

    @Override
    public boolean loseOrEnd() {
        return state == AftermathState.LOSE || state == AftermathState.END;
    }
    @Override
    public SimpleWeightedRandomList<Item> getRewardList() {
        return module.getRewardList();
    }
    @Override
    public void tick() {
        if (isModuleValid() == null || level.players().isEmpty()) end();
        if (loseOrEnd()) return;
        updateProgress();
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag tag = new CompoundTag();
        tag.putUUID("uuid", uuid);
        tag.put("module", module.serializeNBT());
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        uuid = compoundTag.getUUID("uuid");
        module.deserializeNBT(compoundTag.getCompound("module"));
    }
    @Override
    public Predicate<? super ServerPlayer> validPlayer() {
        return (player) -> !player.isSpectator();
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
        progress.getPlayers().forEach(player -> {
            players.add(player.getUUID());
        });

        System.out.println("更新玩家列表 :" + players);
    }

    public void updateProgress() {
        long gameTime = level.getGameTime();
        if (gameTime % 20 == 0) {
            updatePlayers();
        }
    }

    //结束
    @Override
    public void end() {
        setState(AftermathState.END);
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.End(players, level));
        this.progress.removeAllPlayers();
    }

}
