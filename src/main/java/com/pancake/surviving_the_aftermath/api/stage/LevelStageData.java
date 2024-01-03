package com.pancake.surviving_the_aftermath.api.stage;

import com.google.common.collect.Sets;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IStageData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public class LevelStageData extends StageData {
    public LevelStageData() {
        super(Constant.LEVEL_STAGES);
    }
}
