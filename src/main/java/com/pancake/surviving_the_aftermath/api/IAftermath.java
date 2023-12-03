package com.pancake.surviving_the_aftermath.api;

import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;

public interface IAftermath<T extends BaseAftermathModule> extends IIdentifier, INBTSerializable<CompoundTag> {
    void tick();

    default void bindTrackers() {

    }
    List<ITracker> getTrackers();

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
}
