package com.pancake.surviving_the_aftermath.api.module;

import net.minecraft.util.random.SimpleWeightedRandomList;

public interface IWeightedListModule<E> {
    SimpleWeightedRandomList<E> getWeightedList();
}