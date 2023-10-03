package com.pancake.surviving_the_aftermath.api.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.api.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;

public abstract class BaseAftermathModule implements IAftermathModule {
    protected ItemWeightedListModule Rewards;
    @Override
    public SimpleWeightedRandomList<Item> getRewardList() {
        return Rewards.getWeightedList();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.put("rewards", Rewards.serializeNBT());
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        Rewards.deserializeNBT(compoundTag.getCompound("rewards"));
    }

    @Override
    public void deserialize(JsonElement jsonElement) {
        AftermathAPI instance = AftermathAPI.getInstance();
        IWeightedListModule<?> weightedListModule = instance.getWeightedListModule(ItemWeightedListModule.IDENTIFIER);
        if (weightedListModule instanceof ItemWeightedListModule Rewards) {
            Rewards.deserialize(jsonElement.getAsJsonObject().get("rewards"));
            this.Rewards = Rewards;
        }
    }
}
