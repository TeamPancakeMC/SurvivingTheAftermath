package com.pancake.surviving_the_aftermath.api.aftermath;

import com.pancake.surviving_the_aftermath.api.ITracker;
import net.minecraft.nbt.CompoundTag;

import java.util.UUID;

public abstract class BaseTracker implements ITracker {
    protected final AftermathManager manager = AftermathManager.getInstance();
    protected UUID uuid;

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public CompoundTag serializeNBT() {
        return null;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {

    }
}
