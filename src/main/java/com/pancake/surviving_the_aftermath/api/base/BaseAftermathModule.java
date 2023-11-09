package com.pancake.surviving_the_aftermath.api.base;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.common.util.RegistryUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;

import java.util.function.Supplier;

public abstract class BaseAftermathModule implements IAftermathModule {
    protected final AftermathAPI AFTERMATH_API = AftermathAPI.getInstance();
    protected ItemWeightedListModule Rewards;
    protected String jsonName;
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(Constant.IDENTIFIER, getUniqueIdentifier());
        compoundTag.put(Constant.REWARDS, Rewards.serializeNBT());
        return compoundTag;
    }
    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        CompoundTag rewards = compoundTag.getCompound(Constant.REWARDS);
        IWeightedListModule<?> weightedListModule = AFTERMATH_API.getWeightedListModule(ItemWeightedListModule.IDENTIFIER);
        if (weightedListModule instanceof ItemWeightedListModule Rewards) {
            Rewards.deserializeNBT(rewards);
            this.Rewards = Rewards;
        }
    }
    @Override
    public void deserializeJson(JsonElement jsonElement) {
        AftermathAPI instance = AftermathAPI.getInstance();
        IWeightedListModule<?> weightedListModule = instance.getWeightedListModule(ItemWeightedListModule.IDENTIFIER);
        if (weightedListModule instanceof ItemWeightedListModule Rewards) {
            Rewards.deserializeJson(jsonElement.getAsJsonObject().get(Constant.REWARDS));
            this.Rewards = Rewards;
        }
    }
    @Override
    public JsonElement serializeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constant.IDENTIFIER, getUniqueIdentifier());
        jsonObject.add(Constant.REWARDS, Rewards.serializeJson());
        return jsonObject;
    }
    @Override
    public String getJsonName() {
        return jsonName == null ? getUniqueIdentifier().toLowerCase() : jsonName.toLowerCase();
    }
    @Override
    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    public SimpleWeightedRandomList<Item> getRewardList() {
        return Rewards.getWeightedList();
    }
    public void setReward(Supplier<ItemWeightedListModule> rewardList){
        Rewards.setWeightedList(() -> rewardList.get().getWeightedList());
    }
    public void addRewards(String itemStr, int weight){
        Rewards.add(RegistryUtil.getItemFromRegistryName(itemStr),weight);
    }
    public void removeRewards(String itemStr){
        Rewards.remove(RegistryUtil.getItemFromRegistryName(itemStr));
    }
    public static class Builder<T extends BaseAftermathModule> {
        protected  T module;

        public Builder(T module,String jsonName) {
            this.module = module;
            this.module.setJsonName(jsonName);
        }
        protected ItemWeightedListModule Rewards;
        public Builder<T> setRewards(Supplier<ItemWeightedListModule> Rewards) {
            this.Rewards = Rewards.get();
            return this;
        }
        public T build() {
            module.setReward(() -> Rewards);
            return module;
        }
    }
}
