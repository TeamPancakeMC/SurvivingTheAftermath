package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemWeightedModule extends BaseWeightedModule<Item> {
    public static final String IDENTIFIER = "item_weighted";

    public static final Codec<ItemWeightedModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedEntry.Wrapper.codec(BuiltInRegistries.ITEM.byNameCodec())
                    .listOf().fieldOf("list").forGetter(ItemWeightedModule::getList)
    ).apply(instance, ItemWeightedModule::new));

    public ItemWeightedModule(List<WeightedEntry.Wrapper<Item>> list) {
        super(list);
    }

    public ItemWeightedModule() {
    }

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Codec<? extends IWeightedModule<Item>> codec() {
        return CODEC;
    }

    @Override
    public IWeightedModule<Item> type() {
        return ModAftermathModule.ITEM_WEIGHTED.get();
    }

    public static class Builder{
        private List<WeightedEntry.Wrapper<Item>> list;

        public Builder list(List<WeightedEntry.Wrapper<Item>> list) {
            this.list = list;
            return this;
        }

        public ItemWeightedModule build() {
            return new ItemWeightedModule(list);
        }
    }
}
