package com.pancake.surviving_the_aftermath.api;

import net.minecraftforge.common.MinecraftForge;

public interface ITracker extends IIdentifier{
    static void register(ITracker tracker) {
        MinecraftForge.EVENT_BUS.register(tracker);
    }
    static void unregister(ITracker tracker) {
        MinecraftForge.EVENT_BUS.unregister(tracker);
    }

}
