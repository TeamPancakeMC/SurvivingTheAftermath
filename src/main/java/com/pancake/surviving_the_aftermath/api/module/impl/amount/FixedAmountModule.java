package com.pancake.surviving_the_aftermath.api.module.impl.amount;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import net.minecraft.nbt.CompoundTag;


public class FixedAmountModule implements IAmountModule {
    public static final String IDENTIFIER = "FixedAmountModule";
    protected int amount;
    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public int getSpawnAmount() {
        return amount;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(Constant.IDENTIFIER, IDENTIFIER);
        compoundTag.putInt(Constant.AMOUNT, amount);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.amount = nbt.getInt(Constant.AMOUNT);
    }
    @Override
    public void deserializeJson(JsonElement jsonElement) {
        this.amount = jsonElement.getAsJsonObject().get(Constant.AMOUNT).getAsInt();
    }

    @Override
    public JsonElement serializeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constant.IDENTIFIER, IDENTIFIER);
        jsonObject.addProperty(Constant.AMOUNT, amount);
        return jsonObject;
    }

    public static class Builder {
        protected int amount;

        public Builder setAmount(int amount) {
            this.amount = amount;
            return this;
        }

        public FixedAmountModule build() {
            FixedAmountModule fixedAmountModule = new FixedAmountModule();
            fixedAmountModule.amount = this.amount;
            return fixedAmountModule;
        }
    }
}
