package com.pancake.surviving_the_aftermath.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.UUID;

public interface ITracker extends IIdentifier, INBTSerializable<CompoundTag> {
    static void register(ITracker tracker) {
        MinecraftForge.EVENT_BUS.register(tracker);
    }
    static void unregister(ITracker tracker) {
        MinecraftForge.EVENT_BUS.unregister(tracker);
    }

    public ITracker setUUID(UUID uuid);
}
