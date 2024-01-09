package com.pancake.surviving_the_aftermath.api.base;

import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.util.RegistryUtil;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.function.Supplier;

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
        return jsonName;
    }
    @Override
    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }

    public static class Builder<T extends BaseAftermathModule> {
        protected  T module;
        public Builder(T module,String jsonName) {
            this.module = module;
            this.module.setJsonName(jsonName);
        }
        protected ItemWeightedModule Rewards;
        public Builder<T> reward(ItemWeightedModule Rewards) {
            this.Rewards = Rewards;
            return this;
        }
        public T build() {
            module.setRewards(Rewards);
            return module;
        }
    }
}
