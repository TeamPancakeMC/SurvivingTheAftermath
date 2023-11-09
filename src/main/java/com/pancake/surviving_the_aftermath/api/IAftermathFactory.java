package com.pancake.surviving_the_aftermath.api;

import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public interface IAftermathFactory{
    IAftermath<BaseAftermathModule> create(ServerLevel level, CompoundTag compound);
}
