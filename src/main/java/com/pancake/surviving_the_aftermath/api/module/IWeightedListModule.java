package com.pancake.surviving_the_aftermath.api.module;

import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public interface IWeightedListModule<E> {
    SimpleWeightedRandomList<E> getWeightedList();
}
