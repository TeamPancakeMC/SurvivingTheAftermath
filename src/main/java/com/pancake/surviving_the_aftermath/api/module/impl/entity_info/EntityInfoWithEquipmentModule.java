package com.pancake.surviving_the_aftermath.api.module.impl.entity_info;

import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.function.Supplier;

public class EntityInfoWithEquipmentModule extends EntityInfoModule {
    public static final String IDENTIFIER = "entity_info_equipment";
    private ItemWeightedListModule equipment;

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        compoundTag.putString(Constant.IDENTIFIER, IDENTIFIER);
        compoundTag.put("equipment", equipment.serializeNBT());
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);
        IWeightedListModule<?> weightedListModule = AftermathAPI.getInstance().getWeightedListModule(ItemWeightedListModule.IDENTIFIER);
        weightedListModule.deserializeNBT(nbt.getCompound(Constant.EQUIPMENT));
        this.equipment = (ItemWeightedListModule) weightedListModule;
    }
    @Override
    public void deserializeJson(JsonElement jsonElement) {
        super.deserializeJson(jsonElement);
        IWeightedListModule<?> weightedListModule = AftermathAPI.getInstance().getWeightedListModule(ItemWeightedListModule.IDENTIFIER);
        weightedListModule.deserializeJson(jsonElement.getAsJsonObject().get(Constant.EQUIPMENT));
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
                    equipment.getWeightedList().getRandomValue(level.random)
                            .ifPresent(item -> mob.equipItemIfPossible(item.getDefaultInstance()));
                }
            });
        }
        return arrayList;
    }

    @Override
    public JsonElement serializeJson() {
        JsonElement jsonElement = super.serializeJson();
        jsonElement.getAsJsonObject().add(Constant.EQUIPMENT, equipment.serializeJson());
        return jsonElement;
    }

    public static class Builder extends EntityInfoModule.Builder{
        private ItemWeightedListModule equipment;

        public Builder setEquipment(Supplier<ItemWeightedListModule> equipment) {
            this.equipment = equipment.get();
            return this;
        }

        public EntityInfoWithEquipmentModule build() {
            EntityInfoWithEquipmentModule entityInfoWithEquipmentModule = new EntityInfoWithEquipmentModule();
            entityInfoWithEquipmentModule.entityType = this.entityType;
            entityInfoWithEquipmentModule.amountModule = this.amountModule;
            entityInfoWithEquipmentModule.equipment = this.equipment;
            return entityInfoWithEquipmentModule;
        }
    }
}
