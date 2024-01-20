package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.util.CodecUtils;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.effect.MobEffectInstance;

import java.util.List;

public class EffectWeightedModule extends BaseWeightedModule<MobEffectInstance> {
    public static final String IDENTIFIER = "effect_weighted";

    public static final Codec<EffectWeightedModule> CODEC = Codec.list(WeightedEntry.Wrapper.codec(CodecUtils.MOB_EFFECT_INSTANCE_CODEC))
            .xmap(EffectWeightedModule::new, EffectWeightedModule::getList);

    public EffectWeightedModule(List<WeightedEntry.Wrapper<MobEffectInstance>> list) {
        super(list);
    }

    public EffectWeightedModule() {
    }

    @Override
    public Codec<? extends IWeightedModule<MobEffectInstance>> codec() {
        return CODEC;
    }

    @Override
    public IWeightedModule<MobEffectInstance> type() {
        return ModAftermathModule.EFFECT_WEIGHTED.get();
    }
}
