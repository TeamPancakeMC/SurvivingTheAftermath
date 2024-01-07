package com.pancake.surviving_the_aftermath.api.base;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Function;

public abstract class BaseAftermathModule implements IAftermathModule {
    protected ItemWeightedModule Rewards;
    protected String jsonName;

    public BaseAftermathModule(ItemWeightedModule rewards) {
        Rewards = rewards;
    }

    public BaseAftermathModule() {
    }

    public ItemWeightedModule getRewards() {
        return Rewards;
    }
    public List<WeightedEntry.Wrapper<Item>> getRewardsList() {
        return Rewards.getList();
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
}