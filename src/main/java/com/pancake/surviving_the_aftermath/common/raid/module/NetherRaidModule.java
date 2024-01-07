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
            Codec.list(Codec.list(IEntityInfoModule.CODEC.get())).fieldOf("waves").forGetter(NetherRaidModule::getWaves),
            Codec.INT.fieldOf("ready_time").forGetter(NetherRaidModule::getReadyTime)
    ).apply(instance, NetherRaidModule::new));
    private int readyTime;

    public NetherRaidModule(ItemWeightedModule rewards, List<List<IEntityInfoModule>> waves, int readyTime) {
        super(rewards, waves);
        this.readyTime = readyTime;
    }

    public NetherRaidModule() {
    }

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
}