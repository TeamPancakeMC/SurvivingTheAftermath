package com.pancake.surviving_the_aftermath.api.module.impl.weighted;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import com.pancake.surviving_the_aftermath.common.util.RegistryUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;


public class EntityTypeWeightedListModule implements IWeightedListModule<EntityType<?>> {
    public static String IDENTIFIER = "entity_type_weighted";
    protected SimpleWeightedRandomList<EntityType<?>> weightedList;
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
        this.weightedList = builder.build();
    }
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(Constant.IDENTIFIER, IDENTIFIER);
        this.weightedList.unwrap().forEach(itemWeight -> {
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
                    String type = entry.get(Constant.ENTITY_TYPE).getAsString();
                    int weight = entry.get(Constant.WEIGHT).getAsInt();
                    EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(new ResourceLocation(type));
                    if (entityType != null) {
                        builder.add(entityType, weight);
                    }
                });
        this.weightedList = builder.build();
    }

    @Override
    public JsonElement serializeJson() {
        JsonObject jsonObject = new JsonObject();
        this.weightedList.unwrap().forEach(itemWeight -> {
            JsonObject entry = new JsonObject();
            entry.addProperty(Constant.ENTITY_TYPE, RegistryUtil.getRegistryNameFromEntityType(itemWeight.getData()).toString());
            entry.addProperty(Constant.WEIGHT, itemWeight.getWeight().asInt());
            jsonObject.add(RegistryUtil.getRegistryNameFromEntityType(itemWeight.getData()).toString(), entry);
        });
        return jsonObject;
    }

    @Override
    public SimpleWeightedRandomList<EntityType<?>> getWeightedList() {
        return this.weightedList;
    }

    public static class Builder {
        protected SimpleWeightedRandomList.Builder<EntityType<?>> builder = new SimpleWeightedRandomList.Builder<>();

        public Builder add(EntityType<?> entityType, int weight) {
            this.builder.add(entityType, weight);
            return this;
        }

        public EntityTypeWeightedListModule build() {
            EntityTypeWeightedListModule entityTypeWeightedListModule = new EntityTypeWeightedListModule();
            entityTypeWeightedListModule.weightedList = this.builder.build();
            return entityTypeWeightedListModule;
        }
    }
}
