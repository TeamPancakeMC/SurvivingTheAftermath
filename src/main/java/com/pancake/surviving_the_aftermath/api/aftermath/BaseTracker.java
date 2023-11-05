package com.pancake.surviving_the_aftermath.api.aftermath;

import com.pancake.surviving_the_aftermath.api.ITracker;

import java.util.UUID;

public abstract class BaseTracker implements ITracker {
    protected AftermathManager manager = AftermathManager.getInstance();
    protected UUID uuid;

    @Override
    public void setUUID(UUID uuid) {
        this.uuid = uuid;
    }
}
