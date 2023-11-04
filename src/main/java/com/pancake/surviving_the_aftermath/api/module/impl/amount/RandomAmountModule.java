package com.pancake.surviving_the_aftermath.api.module.impl.amount;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class RandomAmountModule implements IAmountModule {
    public static final String IDENTIFIER = "RandomAmountModule";

    private RandomAmountModule() {
    }

    protected final Random rand = new Random();
    protected int min;
    protected int max;
    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public int getSpawnAmount() {
        this.max = Math.max(this.max, this.min);
        return Math.max(0, this.rand.nextInt(this.max - this.min + 1) + this.min);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(Constant.IDENTIFIER, IDENTIFIER);
        compoundTag.putInt(Constant.MIN, min);
        compoundTag.putInt(Constant.MAX, max);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.min = nbt.getInt(Constant.MIN);
        this.max = nbt.getInt(Constant.MAX);
    }
    @Override
    public void deserializeJson(JsonElement jsonElement) {
        this.min = jsonElement.getAsJsonObject().get(Constant.MIN).getAsInt();
        this.max = jsonElement.getAsJsonObject().get(Constant.MAX).getAsInt();
    }

    @Override
    public JsonElement serializeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty(Constant.IDENTIFIER, IDENTIFIER);
        jsonObject.addProperty(Constant.MIN, min);
        jsonObject.addProperty(Constant.MAX, max);
        return jsonObject;
    }

    public static class Builder {
        protected int min;
        protected int max;

        public Builder setRange(int min, int max) {
            this.min = min;
            this.max = max;
            return this;
        }
        public RandomAmountModule build() {
            RandomAmountModule randomAmountModule = new RandomAmountModule();
            randomAmountModule.min = this.min;
            randomAmountModule.max = this.max;
            return randomAmountModule;
        }
    }
}
