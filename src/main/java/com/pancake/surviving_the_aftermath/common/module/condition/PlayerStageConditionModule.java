package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.capability.StageCap;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.world.entity.player.Player;


public class PlayerStageConditionModule extends PlayerConditionModule{
    public static final String IDENTIFIER = "player_stage_condition";
    public static final Codec<PlayerStageConditionModule> CODEC = Codec.STRING.xmap(PlayerStageConditionModule::new, PlayerStageConditionModule::getStage);
    private String stage;

    public PlayerStageConditionModule(String stage) {
        this.stage = stage;
    }

    public PlayerStageConditionModule() {
    }

    @Override
    public boolean checkCondition(Player player) {
        return StageCap.get(player).orElse(new StageCap()).getStages().contains(stage);
    }

    public String getStage() {
        return stage;
    }

    @Override
    public Codec<? extends IConditionModule> codec() {
        return CODEC;
    }

    @Override
    public IConditionModule type() {
        return ModAftermathModule.PLAYER_STAGE_CONDITION.get();
    }
}
