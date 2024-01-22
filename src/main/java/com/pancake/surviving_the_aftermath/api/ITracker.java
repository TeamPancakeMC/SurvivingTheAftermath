package com.pancake.surviving_the_aftermath.api;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import net.minecraftforge.common.MinecraftForge;

import java.util.UUID;
import java.util.function.Supplier;

public interface ITracker extends ICodec<ITracker> {
    Supplier<Codec<ITracker>> CODEC = () -> ModuleRegistry.TRACKER_REGISTRY.get().getCodec()
            .dispatch("tracker", ITracker::type, ITracker::codec);

    static void register(ITracker tracker) {
        MinecraftForge.EVENT_BUS.register(tracker);
    }
    static void unregister(ITracker tracker) {
        MinecraftForge.EVENT_BUS.unregister(tracker);
    }

    ITracker setUUID(UUID uuid);
}