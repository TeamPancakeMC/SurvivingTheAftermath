package com.pancake.surviving_the_aftermath.common.module.amount;


import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;


public class IntegerAmountModule implements IAmountModule {
    public static final String IDENTIFIER = "integer_amount";
    public static final Codec<IntegerAmountModule> CODEC = Codec.INT.xmap(IntegerAmountModule::new, IntegerAmountModule::getAmount);
    protected int amount;

    public IntegerAmountModule(int amount) {
        this.amount = amount;
    }

    public IntegerAmountModule() {
    }
    @Override
    public Codec<? extends IAmountModule> codec() {
        return CODEC;
    }

    @Override
    public int getSpawnAmount() {
        return amount;
    }

    @Override
    public IAmountModule type() {
        return ModAftermathModule.INTEGER_AMOUNT.get();
    }

    public int getAmount() {
        return amount;
    }

    public static class Builder {
        private final int amount;

        public Builder(int amount) {
            this.amount = amount;
        }
        public IntegerAmountModule build() {
            return new IntegerAmountModule(amount);
        }
    }
}
