package com.pancake.surviving_the_aftermath.api.module;

import com.pancake.surviving_the_aftermath.api.IIdentifier;
import com.pancake.surviving_the_aftermath.api.IJSONSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraftforge.common.util.INBTSerializable;

public interface IWeightedListModule<E> extends IJSONSerializable, INBTSerializable<CompoundTag>, IIdentifier {
    SimpleWeightedRandomList<E> getWeightedList();
}