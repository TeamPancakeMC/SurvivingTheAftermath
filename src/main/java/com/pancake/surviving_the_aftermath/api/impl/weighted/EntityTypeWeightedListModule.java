package com.pancake.surviving_the_aftermath.api.impl.weighted;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.api.IJsonDeserialization;
import com.pancake.surviving_the_aftermath.api.IIdentifier;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

public class EntityTypeWeightedListModule implements IWeightedListModule<EntityType<?>>, IJsonDeserialization, INBTSerializable<CompoundTag>, IIdentifier {
    public static final String IDENTIFIER = "EntityTypeWeightedListModule";

    private SimpleWeightedRandomList<EntityType<?>> weightedList;
    @Override
    public SimpleWeightedRandomList<EntityType<?>> getWeightedList() {
        return weightedList;
    }

    @Override
    public void deserialize(JsonElement jsonElement) {
        SimpleWeightedRandomList.Builder<EntityType<?>> builder = new SimpleWeightedRandomList.Builder<>();
        jsonElement.getAsJsonArray().asList().stream()
                .map(JsonElement::getAsJsonObject)
                .forEach(entry -> {
                    String type = GsonHelper.getAsString(entry, "entity_type");
                    int weight = GsonHelper.getAsInt(entry, "weight");
                    EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(type));
                    if (entityType != null) {
                        builder.add(entityType, weight);
                    }
                });
        weightedList = builder.build();
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        SimpleWeightedRandomList.Builder<EntityType<?>> builder = new SimpleWeightedRandomList.Builder<>();
        compoundTag.getAllKeys().forEach(key -> {
            EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(key));
            if (entityType != null) {
                builder.add(entityType, compoundTag.getInt(key));
            }
        });
        weightedList = builder.build();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        weightedList.unwrap().forEach(itemWeight -> {
            ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(itemWeight.getData());
            if (key != null) {
                compoundTag.putInt(key.toString(), itemWeight.getWeight().asInt());
            }
        });
        return compoundTag;
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
