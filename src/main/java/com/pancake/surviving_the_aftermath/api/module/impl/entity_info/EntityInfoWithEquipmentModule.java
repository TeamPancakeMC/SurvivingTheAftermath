package com.pancake.surviving_the_aftermath.api.module.impl.entity_info;

import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.util.RegistryUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class EntityInfoWithEquipmentModule extends BaseEntityInfoModule {
    public static final String IDENTIFIER = "EntityInfoWithEquipmentModule";
    private ItemWeightedListModule equipment;

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        compoundTag.put("equipment", equipment.serializeNBT());
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        IWeightedListModule<?> weightedListModule = AftermathAPI.getInstance().getWeightedListModule(ItemWeightedListModule.IDENTIFIER);
        weightedListModule.deserializeNBT(nbt.getCompound("equipment"));
        this.equipment = (ItemWeightedListModule) weightedListModule;
    }
    @Override
    public void deserializeJson(JsonElement jsonElement) {
        super.deserializeJson(jsonElement);
        IWeightedListModule<?> weightedListModule = AftermathAPI.getInstance().getWeightedListModule(ItemWeightedListModule.IDENTIFIER);
        weightedListModule.deserializeJson(jsonElement.getAsJsonObject().get("equipment"));
        this.equipment = (ItemWeightedListModule) weightedListModule;
    }

    public ItemWeightedListModule getEquipment() {
        return equipment;
    }
}
