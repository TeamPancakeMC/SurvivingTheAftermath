package com.pancake.surviving_the_aftermath.api.module;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.IModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;

import java.util.function.Supplier;

public interface IAftermathModule extends IModule<IAftermathModule> {
    Supplier<Codec<IAftermathModule>> CODEC = () -> ModuleRegistry.AFTERMATH_MODULE_REGISTRY.get().getCodec()
            .dispatch("aftermath_module", IAftermathModule::type, IAftermathModule::codec);
    String getJsonName();
    void setJsonName(String jsonName);
}
