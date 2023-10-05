package com.pancake.surviving_the_aftermath.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;
import java.util.function.Predicate;

public interface IAftermath extends IIdentifier, INBTSerializable<CompoundTag> {
    void tick();
    boolean isEnd();
    boolean isCreate();
    UUID getUUID();

    ResourceLocation getBarsResource();

    int[] getBarsOffset();

    Predicate<? super ServerPlayer> validPlayer();

    void updatePlayers();

    void updateProgress();
    void spawnRewards();

    void end();
}
