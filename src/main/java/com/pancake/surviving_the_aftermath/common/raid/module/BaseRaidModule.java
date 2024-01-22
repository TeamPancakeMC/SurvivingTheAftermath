package com.pancake.surviving_the_aftermath.common.raid.module;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaidModule;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class BaseRaidModule extends BaseAftermathModule implements IRaidModule {
    public static final String IDENTIFIER = "raid";
    public static final Codec<BaseRaidModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.STRING.fieldOf("name").forGetter(BaseRaidModule::getModuleName),
            ItemWeightedModule.CODEC.fieldOf("rewards").forGetter(BaseRaidModule::getRewards),
            Codec.list(IConditionModule.CODEC.get()).fieldOf("conditions").forGetter(BaseRaidModule::getConditions),
            Codec.list(Codec.list(IEntityInfoModule.CODEC.get())).fieldOf("waves").forGetter(BaseRaidModule::getWaves),
            Codec.INT.fieldOf("ready_time").forGetter(BaseRaidModule::getReadyTime),
            Codec.INT.fieldOf("reward_time").forGetter(BaseRaidModule::getRewardTime)
    ).apply(instance, BaseRaidModule::new));

    protected List<List<IEntityInfoModule>> waves;
    protected int readyTime;
    protected int rewardTime;

    public BaseRaidModule(String name,ItemWeightedModule rewards, List<IConditionModule> conditions, List<List<IEntityInfoModule>> waves, int readyTime, int rewardTime) {
        super(name,rewards, conditions);
        this.waves = waves;
        this.readyTime = readyTime;
        this.rewardTime = rewardTime;
    }

    public BaseRaidModule() {
    }

    @Override
    public Codec<? extends IAftermathModule> codec() {
        return CODEC;
    }

    @Override
    public IAftermathModule type() {
        return ModAftermathModule.BASE_RAID_MODULE.get();
    }

    @Override
    public List<List<IEntityInfoModule>> getWaves() {
        return waves;
    }
    public int getReadyTime() {
        return readyTime;
    }
    private int getRewardTime() {
        return rewardTime;
    }
    public BaseRaidModule setWaves(List<List<IEntityInfoModule>> waves) {
        this.waves = waves;
        return this;
    }
    public BaseRaidModule setReadyTime(int readyTime) {
        this.readyTime = readyTime;
        return this;
    }

    public BaseRaidModule setRewardTime(int rewardTime) {
        this.rewardTime = rewardTime;
        return this;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return SurvivingTheAftermath.asResource(IDENTIFIER);
    }

    public static class Builder {
        protected List<IConditionModule> conditions = Lists.newArrayList();
        protected ItemWeightedModule rewards;
        protected String name;


        private List<List<IEntityInfoModule>> waves = Lists.newArrayList();
        private int readyTime;
        private int rewardTime;

        public Builder(String name) {
            this.name = name;
        }

        public Builder rewards(ItemWeightedModule Rewards) {
            this.rewards = Rewards;
            return this;
        }
        public Builder addCondition(IConditionModule module){
            this.conditions.add(module);
            return this;
        }

        public Builder waves(List<List<IEntityInfoModule>> waves) {
            this.waves = waves;
            return this;
        }

        public Builder addWave(List<IEntityInfoModule> waves){
            this.waves.add(waves);
            return this;
        }
        public Builder readyTime(int readyTime) {
            this.readyTime = readyTime;
            return this;
        }
        public Builder rewardTime(int rewardTime) {
            this.rewardTime = rewardTime;
            return this;
        }

        public BaseRaidModule build() {
            return new BaseRaidModule(name,rewards,conditions,waves,readyTime,rewardTime);
        }

    }
}