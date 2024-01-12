package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import net.minecraft.world.entity.player.Player;

public abstract class PlayerConditionModule implements IConditionModule {
    public PlayerConditionModule() {
    }

    public boolean checkCondition(Player player) {
        return true;
    }

}
