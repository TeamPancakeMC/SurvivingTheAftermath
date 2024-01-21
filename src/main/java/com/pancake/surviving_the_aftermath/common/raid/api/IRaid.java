package com.pancake.surviving_the_aftermath.common.raid.api;

import net.minecraft.core.BlockPos;

import java.util.Set;
import java.util.UUID;

public interface IRaid {
    BlockPos getStartPos();
    int getRadius();

    Set<UUID> getEnemies();
}