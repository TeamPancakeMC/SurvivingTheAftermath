package com.pancake.surviving_the_aftermath.api.module.impl.amount;

import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import net.minecraft.nbt.CompoundTag;

import java.util.Random;

public class RandomAmountModule implements IAmountModule {
    public static final String IDENTIFIER = "RandomAmountModule";
    private final Random rand = new Random();
    private int min;
    private int max;
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
        compoundTag.putInt("min", min);
        compoundTag.putInt("max", max);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.min = nbt.getInt("min");
        this.max = nbt.getInt("max");
    }
    @Override
    public void deserializeJson(JsonElement jsonElement) {
        this.min = jsonElement.getAsJsonArray().get(0).getAsInt();
        this.max = jsonElement.getAsJsonArray().get(1).getAsInt();
    }
}
