package com.pancake.surviving_the_aftermath.common.raid.module;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaidModule;

import java.util.List;

public class BaseRaidModule extends BaseAftermathModule implements IRaidModule {
    public static final Codec<BaseRaidModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemWeightedModule.CODEC.fieldOf("rewards").forGetter(BaseRaidModule::getRewards),
            Codec.list(IConditionModule.CODEC.get()).fieldOf("conditions").forGetter(BaseRaidModule::getConditions),
            Codec.list(Codec.list(IEntityInfoModule.CODEC.get())).fieldOf("waves").forGetter(BaseRaidModule::getWaves)
    ).apply(instance, BaseRaidModule::new));
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

    @Override
    public Codec<? extends IAftermathModule> codec() {
        return CODEC;
    }

    @Override
    public IAftermathModule type() {
        return ModAftermathModule.RAID_MODULE.get();
    }
}