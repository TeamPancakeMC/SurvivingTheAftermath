package com.pancake.surviving_the_aftermath.api;

import net.minecraftforge.common.MinecraftForge;

public interface ITracker extends IIdentifier{
    default void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }
    default void unregister() {
        MinecraftForge.EVENT_BUS.unregister(this);
    }
}
