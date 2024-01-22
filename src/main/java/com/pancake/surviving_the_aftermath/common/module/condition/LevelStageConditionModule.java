package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.capability.AftermathStageCap;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class LevelStageConditionModule extends LevelConditionModule{
    public static final String IDENTIFIER = "level_stage_condition";
    public static final Codec<LevelStageConditionModule> CODEC = Codec.STRING.xmap(LevelStageConditionModule::new, LevelStageConditionModule::getStage);
    private String stage ;

    public LevelStageConditionModule(String stage) {
        this.stage = stage;
    }

    public LevelStageConditionModule() {
    }

    @Override
    public boolean checkCondition(Level level, BlockPos pos) {
        return AftermathStageCap.get(level).orElse(new AftermathStageCap()).getStages().contains(stage);
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
        return ModAftermathModule.LEVEL_STAGE_CONDITION.get();
    }

    public static class Builder {
        private final String stage;

        public Builder(String stage) {
            this.stage = stage;
        }

        public LevelStageConditionModule build() {
            return new LevelStageConditionModule(stage);
        }
    }
}
