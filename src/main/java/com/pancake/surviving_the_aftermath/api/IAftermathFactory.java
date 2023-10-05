package com.pancake.surviving_the_aftermath.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public interface IAftermathFactory extends IIdentifier{
    IAftermath create(ServerLevel level, CompoundTag compound);
    IAftermath create(ServerLevel level);
}
