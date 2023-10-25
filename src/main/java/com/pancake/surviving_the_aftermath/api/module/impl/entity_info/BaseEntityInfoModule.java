package com.pancake.surviving_the_aftermath.api.module.impl.entity_info;

import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.util.RegistryUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

public class BaseEntityInfoModule implements IEntityInfoModule {
    public static final String IDENTIFIER = "BaseEntityInfoModule";
    private EntityType<?> entityType;
    private IAmountModule amountModule;

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        ResourceLocation key = ForgeRegistries.ENTITY_TYPES.getKey(entityType);
        assert key != null;
        compoundTag.putString("entity_type", key.getPath());
        compoundTag.put("amount_module", amountModule.serializeNBT());
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.entityType = RegistryUtil.getEntityTypeFromRegistryName(nbt.getString("entity_type"));
        this.amountModule.deserializeNBT(nbt.getCompound("amount_module"));
    }
    @Override
    public void deserializeJson(JsonElement jsonElement) {
        this.entityType = RegistryUtil.getEntityTypeFromRegistryName(jsonElement.getAsJsonObject().get("entity_type").getAsString());
        this.amountModule.deserializeJson(jsonElement.getAsJsonObject().get("amount_module"));
    }
}
