package com.pancake.surviving_the_aftermath.api;

import net.minecraftforge.common.MinecraftForge;

import java.util.UUID;

public interface ITracker extends IIdentifier{
    static void register(ITracker tracker) {
        System.out.println("register tracker");
        MinecraftForge.EVENT_BUS.register(tracker);
    }
    static void unregister(ITracker tracker) {
        System.out.println("unregister tracker");
        MinecraftForge.EVENT_BUS.unregister(tracker);
    }

    public void setUUID(UUID uuid);
}
