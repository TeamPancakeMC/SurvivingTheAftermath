package com.pancake.surviving_the_aftermath.api.module.impl.weighted;

import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.IDeserializationJson;
import com.pancake.surviving_the_aftermath.api.IIdentifier;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.ForgeRegistries;

public class ItemWeightedListModule implements IWeightedListModule<Item>, IDeserializationJson, INBTSerializable<CompoundTag>, IIdentifier {
    public static final String IDENTIFIER = "ItemWeightedListModule";

    private SimpleWeightedRandomList<Item> weightedList;
    @Override
    public SimpleWeightedRandomList<Item> getWeightedList() {
        return weightedList;
    }

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
}
