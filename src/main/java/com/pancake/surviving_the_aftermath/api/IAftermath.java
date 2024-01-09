package com.pancake.surviving_the_aftermath.api;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IAftermath<T extends IAftermathModule> extends ICodec<IAftermath<IAftermathModule>>{
    Supplier<Codec<IAftermath<IAftermathModule>>> CODEC = () -> ModuleRegistry.AFTERMATH_REGISTRY.get().getCodec()
            .dispatch("aftermath", IAftermath::type, IAftermath::codec);


    void tick();
    ResourceLocation getRegistryName();

    default void bindTrackers() {

    }
    Set<ITracker> getTrackers();

    boolean isEnd();
    boolean isLose();
    boolean isCreate();
    UUID getUUID();

    ResourceLocation getBarsResource();

    int[] getBarsOffset();

    Predicate<? super ServerPlayer> validPlayer();

    Set<UUID> getEnemies();

    Set<UUID> getPlayers();

    void updatePlayers();

    void updateProgress();
    void spawnRewards();

    void end();
    void lose();
     T getModule();

    void ready();

    void setServerLevel(ServerLevel level);

}
