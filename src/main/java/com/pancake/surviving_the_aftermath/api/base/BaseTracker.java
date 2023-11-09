package com.pancake.surviving_the_aftermath.api.base;

import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathManager;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public abstract class BaseTracker implements ITracker {
    protected final AftermathManager manager = AftermathManager.getInstance();
    protected UUID uuid;

    @Override
    public ITracker setUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

    @Override
    public CompoundTag serializeNBT() {
        return new CompoundTag();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
