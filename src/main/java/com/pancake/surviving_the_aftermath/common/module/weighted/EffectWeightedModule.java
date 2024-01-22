package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.util.CodecUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraftforge.registries.ForgeRegistries;

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

    public static class Builder {
        private List<WeightedEntry.Wrapper<MobEffectInstance>> effects;

        public Builder add(MobEffectInstance effect, int weight){
            effects.add(WeightedEntry.wrap(effect, weight));
            return this;
        }
        public Builder add(String effect, int duration, int amplifier, int weight){
            effects.add(WeightedEntry.wrap(new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.tryParse(effect)), duration, amplifier), weight));
            return this;
        }

        public EffectWeightedModule build(){
            return new EffectWeightedModule(effects);
        }
    }
}
