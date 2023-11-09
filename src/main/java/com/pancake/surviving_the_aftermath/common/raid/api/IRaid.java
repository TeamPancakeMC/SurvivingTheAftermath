package com.pancake.surviving_the_aftermath.common.raid.api;

import net.minecraft.core.BlockPos;

public interface IRaid {
    BlockPos getCenterPos();
    int getRadius();
}
