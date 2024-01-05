package com.pancake.surviving_the_aftermath.api.base;

import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;

import java.util.function.Function;

public abstract class BaseAftermathModule implements IAftermathModule {
    protected ItemWeightedModule Rewards;
    protected String jsonName;

    public BaseAftermathModule(ItemWeightedModule rewards) {
        Rewards = rewards;
    }

    public ItemWeightedModule getRewards() {
        return Rewards;
    }

    public BaseAftermathModule setRewards(ItemWeightedModule rewards) {
        Rewards = rewards;
        return this;
    }

    @Override
    public String getJsonName() {
        return jsonName == null ? getUniqueIdentifier().toLowerCase() : jsonName.toLowerCase();
    }
    @Override
    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    public static class Builder<T extends BaseAftermathModule> {
        protected T module;
        protected ItemWeightedModule rewards;

        public Builder(T module, String jsonName) {
            this.module = module;
            this.module.setJsonName(jsonName);
        }

        public Builder<T> rewards(ItemWeightedModule rewards) {
            this.rewards = rewards;
            return this;
        }

        public T build() {
            if (rewards != null) {
                module.setRewards(rewards);
            }
            return module;
        }
    }
}