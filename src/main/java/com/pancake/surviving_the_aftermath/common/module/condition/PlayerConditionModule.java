package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;

public abstract class PlayerConditionModule implements IConditionModule {
    public PlayerConditionModule() {
    }

    public abstract boolean checkCondition(Player player);

}
