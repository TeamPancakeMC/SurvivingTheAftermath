package com.pancake.surviving_the_aftermath.api.base;

import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.api.ITracker;

import java.util.UUID;

public abstract class BaseTracker implements ITracker {
    protected final AftermathManager manager = AftermathManager.getInstance();
    protected UUID uuid;

    public BaseTracker() {
    }

    @Override
    public ITracker setUUID(UUID uuid) {
        this.uuid = uuid;
        return this;
    }

}
