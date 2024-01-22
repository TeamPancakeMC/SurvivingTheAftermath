package com.pancake.surviving_the_aftermath.common.module.predicate;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IPredicateModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.EffectWeightedModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class EffectPredicate implements IPredicateModule {
    public static final String IDENTIFIER = "effect";
    public static final Codec<EffectPredicate> CODEC = EffectWeightedModule.CODEC.xmap(EffectPredicate::new, EffectPredicate::getEffects);

    public EffectWeightedModule effects;

    public EffectPredicate(EffectWeightedModule effects) {
        this.effects = effects;
    }
    public EffectPredicate() {
    }
    public EffectWeightedModule getEffects() {
        return effects;
    }

    @Override
    public Codec<? extends IPredicateModule> codec() {
        return CODEC;
    }

    @Override
    public IPredicateModule type() {
        return ModAftermathModule.EFFECT_PREDICATE.get();
    }

    @Override
    public void apply(LivingEntity livingEntity) {
        effects.getWeightedList().getRandomValue(livingEntity.getRandom())
                .ifPresent(livingEntity::addEffect);
    }

    public static class Builder {
        private final List<WeightedEntry.Wrapper<MobEffectInstance>> effectInstances = Lists.newArrayList();

        public Builder add(MobEffectInstance instance,int weight){
            effectInstances.add(WeightedEntry.wrap(instance,weight));
            return this;
        }
        public Builder add(MobEffect mobEffect, int duration, int amplifier ,int weight){
            effectInstances.add(WeightedEntry.wrap(new MobEffectInstance(mobEffect,duration,amplifier),weight));
            return this;
        }
        public Builder add(String mobEffect, int duration, int amplifier ,int weight){
            effectInstances.add(WeightedEntry.wrap(new MobEffectInstance(ForgeRegistries.MOB_EFFECTS.getValue(ResourceLocation.tryParse(mobEffect)),duration,amplifier),weight));
            return this;
        }
        public EffectPredicate build(){
            return new EffectPredicate(new EffectWeightedModule(effectInstances));
        }
    }
}
