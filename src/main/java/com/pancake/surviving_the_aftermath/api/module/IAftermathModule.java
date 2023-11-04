package com.pancake.surviving_the_aftermath.api.module;

import com.pancake.surviving_the_aftermath.api.IJSONSerializable;
import com.pancake.surviving_the_aftermath.api.IIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAftermathModule extends IIdentifier, IJSONSerializable, INBTSerializable<CompoundTag> {
    SimpleWeightedRandomList<Item> getRewardList();
}
