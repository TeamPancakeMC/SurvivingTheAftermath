package com.pancake.surviving_the_aftermath.api.module.impl.weighted;

import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import net.minecraft.util.random.SimpleWeightedRandomList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class BaseWeightedListModule<T> implements IWeightedListModule<T> {
    protected SimpleWeightedRandomList<T> weightedList;
    @Override
    public SimpleWeightedRandomList<T> getWeightedList() {
        return weightedList;
    }

    @Override
    public void setWeightedList(Supplier<SimpleWeightedRandomList<T>> weightedList){
        this.weightedList = weightedList.get();
    }

    @Override
    public void add(T t, int weight) {
        if (this.weightedList == null || t == null) return;
        SimpleWeightedRandomList.Builder<T> builder = SimpleWeightedRandomList.builder();

        this.weightedList.unwrap()
                .forEach(itemWeight -> builder.add(itemWeight.getData(), itemWeight.getWeight().asInt()));
        builder.add(t, weight);

        this.weightedList = builder.build();
    }

    @Override
    public void remove(T t) {
        if (this.weightedList == null || t == null) return;
        SimpleWeightedRandomList.Builder<T> builder = SimpleWeightedRandomList.builder();

        this.weightedList.unwrap().stream()
                .filter(entry -> !(entry.getData().equals(t)))
                .forEach(itemWeight -> builder.add(itemWeight.getData(), itemWeight.getWeight().asInt()));

        this.weightedList = builder.build();
    }

    public List<T> getList(){
        ArrayList<T> list = Lists.newArrayList();
        getWeightedList().unwrap().forEach(weightedEntry -> list.add(weightedEntry.getData()));
        return list;
    }
}
