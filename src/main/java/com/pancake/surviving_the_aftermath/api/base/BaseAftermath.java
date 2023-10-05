package com.pancake.surviving_the_aftermath.api.base;

import com.google.common.collect.Sets;
import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathManager;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.event.AftermathEvent;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraftforge.common.MinecraftForge;
import org.slf4j.Logger;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public abstract class BaseAftermath implements IAftermath {
    protected static final Logger LOGGER = LogUtils.getLogger();
    protected final AftermathAPI AFTERMATH_API = AftermathAPI.getInstance();
    protected final AftermathManager AFTERMATH_MANAGER = AftermathManager.getInstance();
    protected AftermathState state;
    protected UUID uuid;
    protected ServerLevel level;
    protected Set<UUID> players = new HashSet<>();
    protected IAftermathModule module;
    private final String NAME = Constant.MOD_NAME + "." + getUniqueIdentifier();
    protected final ServerBossEvent progress = new ServerBossEvent(Component.translatable(NAME), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);

    public BaseAftermath(ServerLevel level) {
        this.level = level;
        this.uuid = progress.getId();
        this.module = AFTERMATH_API.getRandomAftermathModule(getUniqueIdentifier()).orElseGet(() -> AFTERMATH_API.getAftermathMap().get(getUniqueIdentifier()).get(0));
    }
    public BaseAftermath(ServerLevel level, CompoundTag compoundTag) {
        this.level = level;
        this.deserializeNBT(compoundTag);
    }

    @Override
    public void tick() {
        if (level.players().isEmpty()) {
            LOGGER.error("level not found player");
            end();
            return;
        }
        if (isEnd()) return;
        updateProgress();
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

        ListTag tags = new ListTag();
        this.players.forEach(uuid -> tags.add(NbtUtils.createUUID(uuid)));
        compoundTag.put(Constant.PLAYERS, tags);

        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.uuid = nbt.getUUID(Constant.UUID);

        CompoundTag moduleTag = nbt.getCompound(Constant.MODULE);
        IAftermathModule aftermathModule = AFTERMATH_API.getAftermathModule(this.getUniqueIdentifier());
        aftermathModule.deserializeNBT(moduleTag);
        this.module = aftermathModule;

        ListTag tags = nbt.getList(Constant.PLAYERS, 11);
        players.clear();
        tags.forEach(tag -> this.players.add(NbtUtils.loadUUID(tag)));
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
    }

    @Override
    public void updateProgress() {
        long gameTime = level.getGameTime();
        if (gameTime % 20 == 0) {
            updatePlayers();
        }
    }

    @Override
    public void end() {
        this.state = AftermathState.END;
        MinecraftForge.EVENT_BUS.post(new AftermathEvent.End(players, level));
        this.progress.removeAllPlayers();
    }
}
