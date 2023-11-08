package com.pancake.surviving_the_aftermath.common.mixin;

import com.pancake.surviving_the_aftermath.api.PortalShapeAccessor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.portal.PortalShape;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PortalShape.class)
public class PortalShapeMixin implements PortalShapeAccessor {
    //bottomLeft
    @Shadow
    private BlockPos bottomLeft;

    //height
    @Shadow
    private int height;

    //width
    @Final
    @Shadow
    private int width;

    //rightDir
    @Final
    @Shadow
    private Direction rightDir;


    @Override
    public BlockPos survivingTheAftermath$getBottomLeft() {
        return bottomLeft;
    }

    @Override
    public int survivingTheAftermath$getHeight() {
        return height;
    }

    @Override
    public int survivingTheAftermath$getWidth() {
        return width;
    }

    @Override
    public Direction survivingTheAftermath$getRightDir() {
        return rightDir;
    }
}


