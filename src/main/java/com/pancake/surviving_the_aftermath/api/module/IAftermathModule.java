package com.pancake.surviving_the_aftermath.api.module;

import com.pancake.surviving_the_aftermath.api.IIdentifier;
import com.pancake.surviving_the_aftermath.api.IJSONSerializable;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.function.Supplier;

public interface IAftermathModule extends IIdentifier, IJSONSerializable, INBTSerializable<CompoundTag> {
    String getJsonName();

    void setJsonName(String jsonName);
}
