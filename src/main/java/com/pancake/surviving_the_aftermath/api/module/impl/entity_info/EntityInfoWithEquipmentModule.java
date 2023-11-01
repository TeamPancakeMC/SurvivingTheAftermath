package com.pancake.surviving_the_aftermath.api.module.impl.entity_info;

import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.util.LazyOptional;

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

    @Override
    public List<LazyOptional<Entity>> spawnEntity(ServerLevel level) {
        List<LazyOptional<Entity>> arrayList = super.spawnEntity(level);
        for (LazyOptional<Entity> lazyOptional : arrayList) {
            lazyOptional.ifPresent(entity -> {
                if (entity instanceof Mob mob){
                    equipment.getWeightedList().getRandomValue(level.random).ifPresent(item -> {
                        mob.equipItemIfPossible(item.getDefaultInstance());
                    });
                }
            });
        }
        return arrayList;
    }
}
