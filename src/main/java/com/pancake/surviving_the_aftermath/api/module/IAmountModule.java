package com.pancake.surviving_the_aftermath.api.module;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.IModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;

import java.util.function.Supplier;

public interface IAmountModule extends IModule<IAmountModule> {
    Supplier<Codec<IAmountModule>> CODEC = () -> ModuleRegistry.AMOUNT_REGISTRY.get().getCodec()
            .dispatch("amount", IAmountModule::type, IAmountModule::codec);
    int getSpawnAmount();


}
