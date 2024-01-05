package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;

public class StageConditionModule implements IConditionModule {
    public static final String IDENTIFIER = "stage_condition";
    public static final Codec<StageConditionModule> CODEC = Codec.STRING.xmap(StageConditionModule::new, StageConditionModule::getStage);
    public String stage;

    public StageConditionModule(String stage) {
        this.stage = stage;
    }

    public StageConditionModule() {
    }
    @Override
    public Codec<? extends IConditionModule> codec() {
        return null;
    }

    @Override
    public IConditionModule type() {
        return null;
    }

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    public String getStage() {
        return stage;
    }

    public static class Builder {
        private String stage;

        public Builder stage(String stage) {
            this.stage = stage;
            return this;
        }

        public StageConditionModule build() {
            return new StageConditionModule(stage);
        }
    }
}
