package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class LevelConditionModule implements IConditionModule {
    public LevelConditionModule() {
    }

    public boolean checkCondition(Level level, BlockPos pos) {
        return true;
    }
}
