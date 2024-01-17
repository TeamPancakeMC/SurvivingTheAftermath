package com.pancake.surviving_the_aftermath.api;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

@FunctionalInterface
public interface SpawnPosHandler {
    void handleSpawnPos(Level level, BlockPos pos);
}