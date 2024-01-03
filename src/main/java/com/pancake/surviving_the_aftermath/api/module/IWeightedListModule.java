package com.pancake.surviving_the_aftermath.api.module;

import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.api.IIdentifier;
import com.pancake.surviving_the_aftermath.api.IJSONSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraftforge.common.util.INBTSerializable;
import org.checkerframework.checker.nullness.qual.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public interface IWeightedListModule<T> extends IJSONSerializable, INBTSerializable<CompoundTag>, IIdentifier {
    SimpleWeightedRandomList<T> getWeightedList();

    void setWeightedList(Supplier<SimpleWeightedRandomList<T>> weightedList);
    void add(T t, int weight);
    void remove(T t);

    List<T> getList();
}