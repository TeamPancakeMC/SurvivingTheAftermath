package com.pancake.surviving_the_aftermath.api;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;

public interface PortalShapeAccessor {
    //bottomLeft
    BlockPos survivingTheAftermath$getBottomLeft();

    //height
    int survivingTheAftermath$getHeight();

    //width
    int survivingTheAftermath$getWidth();

    //rightDir
    Direction survivingTheAftermath$getRightDir();
}
