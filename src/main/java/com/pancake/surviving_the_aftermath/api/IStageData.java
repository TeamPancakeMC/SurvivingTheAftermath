package com.pancake.surviving_the_aftermath.api;

import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.Collection;

public interface IStageData extends IIdentifier, INBTSerializable<CompoundTag> {
    Collection<String> getStages();

    boolean hasStage(String stage);

    void addStage(String stage);

    void removeStage(String stage);

    void clear();


}
