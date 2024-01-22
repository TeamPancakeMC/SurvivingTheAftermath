package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.util.RegistryUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

import java.util.List;

public class ItemWeightedModule extends BaseWeightedModule<Item> {
    public static final String IDENTIFIER = "item_weighted";

    public static final Codec<ItemWeightedModule> CODEC = Codec.list(WeightedEntry.Wrapper.codec(BuiltInRegistries.ITEM.byNameCodec()))
            .xmap(ItemWeightedModule::new, ItemWeightedModule::getList);


    public ItemWeightedModule(List<WeightedEntry.Wrapper<Item>> list) {
        super(list);
    }

    public ItemWeightedModule() {
    }

    @Override
    public Codec<? extends IWeightedModule<Item>> codec() {
        return CODEC;
    }

    @Override
    public IWeightedModule<Item> type() {
        return ModAftermathModule.ITEM_WEIGHTED.get();
    }

    public static class Builder {
        private List<WeightedEntry.Wrapper<Item>> list = Lists.newArrayList();
        public ItemWeightedModule.Builder add(Item item, int weight) {
            this.list.add(WeightedEntry.wrap(item, weight));
            return this;
        }

        public ItemWeightedModule.Builder add(String item, int weight) {
            this.list.add(WeightedEntry.wrap(RegistryUtil.getItemFromRegistryName(item), weight));
            return this;
        }
        public ItemWeightedModule build() {
            return new ItemWeightedModule(list);
        }
    }
}
