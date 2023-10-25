package com.pancake.surviving_the_aftermath.api.module.impl.weighted;

import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;


public class EntityTypeWeightedListModule implements IWeightedListModule<EntityType<?>> {
    public static String IDENTIFIER = "EntityTypeWeightedListModule";
    private SimpleWeightedRandomList<EntityType<?>> weightedList;
    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
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
    public void deserializeJson(JsonElement jsonElement) {
        SimpleWeightedRandomList.Builder<EntityType<?>> builder = new SimpleWeightedRandomList.Builder<>();
        jsonElement.getAsJsonArray().asList().stream()
                .map(JsonElement::getAsJsonObject)
                .forEach(entry -> {
                    String type = entry.get("entity_type").getAsString();
                    int weight = entry.get("weight").getAsInt();
                    EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(type));
                    if (entityType != null) {
                        builder.add(entityType, weight);
                    }
                });
        weightedList = builder.build();
    }

    @Override
    public SimpleWeightedRandomList<EntityType<?>> getWeightedList() {
        return this.weightedList;
    }
}
