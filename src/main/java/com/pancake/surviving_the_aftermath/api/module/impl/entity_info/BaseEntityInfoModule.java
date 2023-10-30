package com.pancake.surviving_the_aftermath.api.module.impl.entity_info;

import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.util.RegistryUtil;
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
        compoundTag.putString(Constant.ENTITY_TYPE, key.getPath());
        compoundTag.put(Constant.AMOUNT, amountModule.serializeNBT());
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.entityType = RegistryUtil.getEntityTypeFromRegistryName(nbt.getString(Constant.ENTITY_TYPE));
        CompoundTag compound = nbt.getCompound(Constant.AMOUNT);
        IAmountModule amountModule = AftermathAPI.getInstance().getAmountModule(compound.getString(Constant.IDENTIFIER));
        amountModule.deserializeNBT(compound);
        this.amountModule = amountModule;
    }
    @Override
    public void deserializeJson(JsonElement jsonElement) {
        this.entityType = RegistryUtil.getEntityTypeFromRegistryName(jsonElement.getAsJsonObject().get(Constant.ENTITY_TYPE).getAsString());
        JsonElement amountElement = jsonElement.getAsJsonObject().get(Constant.AMOUNT);
        IAmountModule amountModule = AftermathAPI.getInstance().getAmountModule(amountElement.getAsJsonObject().get(Constant.IDENTIFIER).getAsString());
        amountModule.deserializeJson(amountElement);
        this.amountModule = amountModule;
    }
}
