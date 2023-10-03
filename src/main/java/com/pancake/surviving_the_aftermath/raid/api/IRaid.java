package com.pancake.surviving_the_aftermath.raid.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;

public interface IRaid {
    int getRadius();
    BlockPos getCenterPos();
}
