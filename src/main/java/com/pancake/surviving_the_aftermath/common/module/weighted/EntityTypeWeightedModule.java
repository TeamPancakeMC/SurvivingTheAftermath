package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

public class EntityTypeWeightedModule extends BaseWeightedModule<EntityType<?>> {
    public static final String IDENTIFIER = "entity_type_weighted";

    public static final Codec<List<WeightedEntry.Wrapper<EntityType<?>>>> CODEC = WeightedEntry.Wrapper.codec(BuiltInRegistries.ENTITY_TYPE.byNameCodec())
            .listOf().comapFlatMap(list -> {
                List<WeightedEntry.Wrapper<EntityType<?>>> filteredList = list.stream()
                        .filter(wrapper -> ForgeRegistries.ENTITY_TYPES.containsKey(ForgeRegistries.ENTITY_TYPES.getKey(wrapper.getData())))
                        .collect(Collectors.toList());
                return DataResult.success(filteredList);
            }, list -> list);

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }
}
