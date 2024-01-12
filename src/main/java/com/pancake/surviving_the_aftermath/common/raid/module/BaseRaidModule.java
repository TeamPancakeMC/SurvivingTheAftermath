package com.pancake.surviving_the_aftermath.common.raid.module;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaidModule;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

public abstract class BaseRaidModule extends BaseAftermathModule implements IRaidModule {
    protected List<List<IEntityInfoModule>> waves ;

    public BaseRaidModule(ItemWeightedModule rewards, List<IConditionModule> conditions, List<List<IEntityInfoModule>> waves) {
        super(rewards, conditions);
        this.waves = waves;
    }

    public BaseRaidModule() {
    }

    @Override
    public List<List<IEntityInfoModule>> getWaves() {
        return waves;
    }

    public BaseRaidModule setWaves(List<List<IEntityInfoModule>> waves) {
        this.waves = waves;
        return this;
    }
}