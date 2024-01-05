package com.pancake.surviving_the_aftermath.common.raid.module;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;

import java.util.List;

public class NetherRaidModule extends BaseRaidModule {
    public static final Codec<NetherRaidModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemWeightedModule.CODEC.fieldOf("rewards").forGetter(NetherRaidModule::getRewards),
            Codec.list(Codec.list(ModuleRegistry.Codecs.ENTITY_INFO_CODEC.get())).fieldOf("waves").forGetter(NetherRaidModule::getWaves),
            Codec.INT.fieldOf("ready_time").forGetter(NetherRaidModule::getReadyTime)
    ).apply(instance, NetherRaidModule::new));

    public NetherRaidModule(ItemWeightedModule rewards, List<List<IEntityInfoModule>> waves, int readyTime) {
        super(rewards, waves);
        this.readyTime = readyTime;
    }

    public NetherRaidModule() {
        super(new ItemWeightedModule(), Lists.newArrayList());
    }

    private int readyTime;
    @Override
    public Codec<? extends IAftermathModule> codec() {
        return CODEC;
    }

    @Override
    public IAftermathModule type() {
        return ModAftermathModule.NETHER_RAID.get();
    }

    @Override
    public String getUniqueIdentifier() {
        return NetherRaid.IDENTIFIER;
    }

    public int getReadyTime() {
        return readyTime;
    }

    public NetherRaidModule setReadyTime(int readyTime) {
        this.readyTime = readyTime;
        return this;
    }

    public static class Builder extends BaseRaidModule.Builder<NetherRaidModule>{
        private int readyTime;

        public Builder(String jsonName) {
            super(new NetherRaidModule(), jsonName);
        }

        public Builder readyTime(int readyTime) {
            this.readyTime = readyTime;
            return this;
        }

        @Override
        public NetherRaidModule build() {
            NetherRaidModule build = super.build();
            build.setReadyTime(this.readyTime);
            return build;
        }

    }
}