package com.pancake.surviving_the_aftermath.api.module;

import com.pancake.surviving_the_aftermath.api.IIdentifier;
import com.pancake.surviving_the_aftermath.api.IJSONSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Supplier;

public interface IWeightedListModule<E> extends IJSONSerializable, INBTSerializable<CompoundTag>, IIdentifier {
    SimpleWeightedRandomList<E> getWeightedList();

    void setWeightedList(Supplier<SimpleWeightedRandomList<E>> weightedList);
    void add(E e, int weight);
    void remove(E e);
}