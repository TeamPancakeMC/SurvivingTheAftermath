package com.pancake.surviving_the_aftermath.api.module;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import com.pancake.surviving_the_aftermath.api.IModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.amount.IntegerAmountModule;
import com.pancake.surviving_the_aftermath.common.module.amount.RandomAmountModule;

import java.util.function.Function;

public interface IAmountModule extends IModule {
    public static final Codec<IAmountModule> CODEC = null;
    Codec<? extends IAmountModule> codec();
    abstract int getSpawnAmount();

    abstract IAmountModule type();
}
