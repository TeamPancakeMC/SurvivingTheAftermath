package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;

import java.util.List;
import java.util.stream.Collectors;

public class ItemWeightedModule extends BaseWeightedModule<Item> {
    public static final String IDENTIFIER = "item_weighted";
    public static final Codec<List<WeightedEntry.Wrapper<Item>>> CODEC = WeightedEntry.Wrapper.codec(BuiltInRegistries.ITEM.byNameCodec())
            .listOf().comapFlatMap(list -> {
                List<WeightedEntry.Wrapper<Item>> filteredList = list.stream()
                        .filter(wrapper -> BuiltInRegistries.ITEM.containsKey(BuiltInRegistries.ITEM.getKey(wrapper.getData())))
                        .collect(Collectors.toList());
                return DataResult.success(filteredList);
            }, list -> list);
    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }
}
