package com.pancake.surviving_the_aftermath.api.module;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.ICodec;
import com.pancake.surviving_the_aftermath.api.IModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;

import java.util.function.Supplier;

public interface IEntityInfoModule extends IModule<IEntityInfoModule> {
    Supplier<Codec<IEntityInfoModule>> CODEC = () -> ModuleRegistry.ENTITY_INFO_REGISTRY.get().getCodec()
            .dispatch("entity_info", IEntityInfoModule::type, IEntityInfoModule::codec);

}
