package com.pancake.surviving_the_aftermath.common.raid.module;

import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaidModule;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseRaidModule extends BaseAftermathModule implements IRaidModule {
    protected List<List<IEntityInfoModule>> waves ;

    public BaseRaidModule(ItemWeightedModule rewards, List<List<IEntityInfoModule>> waves) {
        super(rewards);
        this.waves = waves;
    }

    @Override
    public List<List<IEntityInfoModule>> getWaves() {
        return waves;
    }

    public BaseRaidModule setWaves(List<List<IEntityInfoModule>> waves) {
        this.waves = waves;
        return this;
    }

    public static class Builder<T extends BaseAftermathModule> extends BaseAftermathModule.Builder<T>{
        protected List<List<IEntityInfoModule>> waves = Lists.newArrayList();

        public Builder(T module, String jsonName) {
            super(module, jsonName);
        }

        public Builder<T> addWave(List<IEntityInfoModule> wave) {
            this.waves.add(wave);
            return this;
        }

        public Builder<T> addWaves(List<List<IEntityInfoModule>> waves) {
            this.waves.addAll(waves);
            return this;
        }

        @Override
        public T build() {
            T module = super.build();
            if (module instanceof BaseRaidModule baseRaidModule) {
                baseRaidModule.setWaves(waves);
            }
            return module;
        }

    }
}