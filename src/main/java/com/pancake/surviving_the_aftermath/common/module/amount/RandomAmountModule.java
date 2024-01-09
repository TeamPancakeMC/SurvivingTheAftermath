package com.pancake.surviving_the_aftermath.common.module.amount;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;

import java.util.Random;

public class RandomAmountModule implements IAmountModule {
    public static final String IDENTIFIER = "random_amount";
    public static final Codec<RandomAmountModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("min").forGetter(RandomAmountModule::getMin),
            Codec.INT.fieldOf("max").forGetter(RandomAmountModule::getMax)
    ).apply(instance, RandomAmountModule::new));

    @Override
    public Codec<? extends IAmountModule> codec() {
        return CODEC;
    }
//    @Override
//    public IAmountModule type() {
//        return this;
//    }
    private final Random rand = new Random();
    protected int min;
    protected int max;

    public RandomAmountModule(int min, int max) {
        this.min = min;
        this.max = max;
    }

    public RandomAmountModule() {
    }

    @Override
    public int getSpawnAmount() {
        this.max = Math.max(this.max, this.min);
        return Math.max(0, this.rand.nextInt(this.max - this.min + 1) + this.min);
    }

    @Override
    public IAmountModule type() {
        return ModAftermathModule.RANDOM_AMOUNT.get();
    }


    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public static class Builder {
        private final int min;
        private final int max;

        public Builder(int min, int max) {
            this.min = min;
            this.max = max;
        }
        public RandomAmountModule build() {
            return new RandomAmountModule(min, max);
        }
    }
}