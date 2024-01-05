package com.pancake.surviving_the_aftermath.api.module;

import com.pancake.surviving_the_aftermath.api.ICodec;
import com.pancake.surviving_the_aftermath.api.IModule;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;

import java.util.List;

public interface IWeightedModule<T> extends IModule<IWeightedModule<T>> {
    SimpleWeightedRandomList<T> getWeightedList();
    void add(T t, int weight);
    void remove(T t);

    List<WeightedEntry.Wrapper<T>> getList();
}
