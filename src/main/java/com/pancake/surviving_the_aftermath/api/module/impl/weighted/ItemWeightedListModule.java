package com.pancake.surviving_the_aftermath.api.module.impl.weighted;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import com.pancake.surviving_the_aftermath.common.util.RegistryUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.function.Supplier;

public class ItemWeightedListModule extends BaseWeightedListModule<Item>{

    public static final String IDENTIFIER = "item_weighted";

    @Override
    public void deserializeJson(JsonElement jsonElement) {
        SimpleWeightedRandomList.Builder<Item> builder = new SimpleWeightedRandomList.Builder<>();
        jsonElement.getAsJsonArray().asList().stream()
                .map(JsonElement::getAsJsonObject)
                .forEach(entry -> {
                    String type = GsonHelper.getAsString(entry, "item");
                    int weight = GsonHelper.getAsInt(entry, "weight");
                    Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(type));
                    if (item != null) {
                        builder.add(item, weight);
                    }
                });
        weightedList = builder.build();
    }

    @Override
    public JsonElement serializeJson() {
        JsonArray jsonElements = new JsonArray();
        weightedList.unwrap().forEach(itemWeight -> {
            JsonObject jsonObject = new JsonObject();
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(itemWeight.getData());
            if (key != null) {
                jsonObject.addProperty(Constant.ITEM, key.toString());
                jsonObject.addProperty(Constant.WEIGHT, itemWeight.getWeight().asInt());
            }
            jsonElements.add(jsonObject);
        });
        return jsonElements;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        SimpleWeightedRandomList.Builder<Item> builder = new SimpleWeightedRandomList.Builder<>();
        compoundTag.getAllKeys().forEach(key -> {
            Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(key));
            if (item != null) {
                builder.add(item, compoundTag.getInt(key));
            }
        });
        weightedList = builder.build();
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(Constant.IDENTIFIER, IDENTIFIER);
        weightedList.unwrap().forEach(itemWeight -> {
            ResourceLocation key = ForgeRegistries.ITEMS.getKey(itemWeight.getData());
            if (key != null) {
                compoundTag.putInt(key.toString(), itemWeight.getWeight().asInt());
            }
        });
        return compoundTag;
    }

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    public static class Builder {
        private final SimpleWeightedRandomList.Builder<Item> builder = new SimpleWeightedRandomList.Builder<>();

        public Builder add(String itemStr, int weight) {
            Item item = RegistryUtil.getItemFromRegistryName(itemStr);
            builder.add(item, weight);
            return this;
        }

        public ItemWeightedListModule build() {
            ItemWeightedListModule itemWeightedListModule = new ItemWeightedListModule();
            itemWeightedListModule.weightedList = builder.build();
            return itemWeightedListModule;
        }
    }
}
