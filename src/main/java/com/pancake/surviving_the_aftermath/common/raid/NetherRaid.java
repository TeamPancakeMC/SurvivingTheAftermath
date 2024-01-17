package com.pancake.surviving_the_aftermath.common.raid;

import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.PortalShapeAccessor;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.portal.PortalShape;

import java.util.*;

public class NetherRaid extends BaseRaid {
    public static final String IDENTIFIER = "nether_raid";
    private PortalShape portalShape;
    public NetherRaid(ServerLevel level, BlockPos startPos,PortalShape portalShape) {
        super(level, startPos);
        this.portalShape = portalShape;
    }

    public NetherRaid() {
    }

    @Override
    public void setMobSpawnPos(ServerLevel serverLevel, String metadata, BlockPos startPos, BlockPos metaPos) {
        Optional<PortalShape> optional = PortalShape.findEmptyPortalShape(serverLevel, metaPos, Direction.Axis.X);
        if (optional.isPresent()) {
            PortalShapeAccessor portalShapeMixin = (PortalShapeAccessor) optional.get();
            BlockPos bottomLeft = portalShapeMixin.survivingTheAftermath$getBottomLeft();
            int height = portalShapeMixin.survivingTheAftermath$getHeight();
            int width = portalShapeMixin.survivingTheAftermath$getWidth();
            Direction rightDir = portalShapeMixin.survivingTheAftermath$getRightDir();
            BlockPos.betweenClosed(bottomLeft, bottomLeft.relative(Direction.UP, height - 1).relative(rightDir, width - 1))
                    .forEach(blockPos -> spawnPos.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
        }
    }
}
