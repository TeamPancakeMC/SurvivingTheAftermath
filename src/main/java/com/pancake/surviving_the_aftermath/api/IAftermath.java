package com.pancake.surviving_the_aftermath.api;

import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;
import java.util.function.Predicate;

public interface IAftermath extends IIdentifier, INBTSerializable<CompoundTag> {
    boolean isCreate();
    void tick();

    boolean loseOrEnd();
    AftermathState getState();

    void setState(AftermathState state);
    default IAftermathModule isModuleValid() {
        return null;
    }

    SimpleWeightedRandomList<Item> getRewardList();

    UUID getUUID();
    void spawn(ServerLevel level, BlockPos spawnPos);
    void updatePlayers();
    void deserializeNBT(CompoundTag compoundTag);

    Predicate<? super ServerPlayer> validPlayer();

    //结束
    void end();
}
