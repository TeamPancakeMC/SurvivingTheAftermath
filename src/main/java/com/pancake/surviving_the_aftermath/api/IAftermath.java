package com.pancake.surviving_the_aftermath.api;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IAftermath<T extends IAftermathModule> extends IModule<IAftermath<T>> {
    Supplier<Codec<IAftermath<IAftermathModule>>> CODEC = () -> ModuleRegistry.AFTERMATH_REGISTRY.get().getCodec()
            .dispatch("aftermath", IAftermath::type, IAftermath::codec);
    boolean isCreate(Level level, BlockPos pos,Player player);
    void tick();
    boolean isEnd();
    boolean isLose();
    void ready();
    void end();
    void lose();
    void updatePlayers();
    void updateProgress();
    void spawnRewards();
    Predicate<? super ServerPlayer> validPlayer();

    ResourceLocation getRegistryName();
    AftermathState getState();
    Level getLevel();
    Set<UUID> getPlayers();
    Set<UUID> getEnemies();
    IAftermathModule getModule();
    UUID getUUID();
    float getProgressPercent();



    void insertTag(LivingEntity entity);
    void setLevel(Level level);
}
