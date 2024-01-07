package com.pancake.surviving_the_aftermath.api.module;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.IModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;

import java.util.List;
import java.util.function.Supplier;

public interface IWeightedModule<T> extends IModule<IWeightedModule<T>> {
    Supplier<Codec<IWeightedModule<?>>> CODEC = () -> ModuleRegistry.WEIGHTED_REGISTRY.get().getCodec()
            .dispatch("weighted", IWeightedModule::type, IWeightedModule::codec);
    SimpleWeightedRandomList<T> getWeightedList();
    void add(T t, int weight);
    void remove(T t);

    List<WeightedEntry.Wrapper<T>> getList();
}
