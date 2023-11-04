package com.pancake.surviving_the_aftermath.api.module;

import com.pancake.surviving_the_aftermath.api.IJSONSerializable;
import com.pancake.surviving_the_aftermath.api.IIdentifier;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

public interface IAmountModule extends IIdentifier, IJSONSerializable, INBTSerializable<CompoundTag> {
    int getSpawnAmount();
}
