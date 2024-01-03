package com.pancake.surviving_the_aftermath.api.module;

import com.pancake.surviving_the_aftermath.api.IIdentifier;
import com.pancake.surviving_the_aftermath.api.IJSONSerializable;
import com.pancake.surviving_the_aftermath.api.stage.LevelStageData;
import com.pancake.surviving_the_aftermath.api.stage.PlayerStageData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;

public interface IConditionModule extends IIdentifier, IJSONSerializable, INBTSerializable<CompoundTag> {
//    boolean checkCondition(Player player, PlayerStageData stageData);
}
