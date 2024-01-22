package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.util.random.WeightedEntry.Wrapper;

import java.util.List;


public abstract class BaseWeightedModule<T> implements IWeightedModule<T> {
    protected List<WeightedEntry.Wrapper<T>> list;

    public BaseWeightedModule(List<Wrapper<T>> list) {
        this.list = list;
    }

    public BaseWeightedModule() {
    }

    @Override
    public void add(T t, int weight) {
        this.list.add(WeightedEntry.wrap(t, weight));
    }

    @Override
    public void remove(T t) {
        this.list.removeIf(wrapper -> wrapper.getData().equals(t));
    }

    @Override
    public SimpleWeightedRandomList<T> getWeightedList() {
        SimpleWeightedRandomList.Builder<T> builder = SimpleWeightedRandomList.builder();
        this.list.forEach(build -> builder.add(build.getData(), build.getWeight().asInt()));
        return builder.build();
    }

    @Override
    public List<Wrapper<T>> getList() {
        return this.list;
    }
}