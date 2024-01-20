package com.pancake.surviving_the_aftermath.common.module.predicate;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IPredicateModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.AttributeWeightedModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.EffectWeightedModule;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;

import java.util.List;
import java.util.UUID;

public class AttributePredicate implements IPredicateModule {
    public static final String IDENTIFIER = "attribute";
    public static final Codec<AttributePredicate> CODEC = AttributeWeightedModule.CODEC.xmap(AttributePredicate::new, AttributePredicate::getAttribute);

    public AttributeWeightedModule attribute;

    public AttributePredicate(AttributeWeightedModule attribute) {
        this.attribute = attribute;
    }

    public AttributePredicate() {
    }

    public AttributeWeightedModule getAttribute() {
        return attribute;
    }


    @Override
    public Codec<? extends IPredicateModule> codec() {
        return CODEC;
    }

    @Override
    public IPredicateModule type() {
        return ModAftermathModule.ATTRIBUTE_PREDICATE.get();
    }

    @Override
    public void apply(LivingEntity livingEntity) {
        attribute.getWeightedList().getRandomValue(livingEntity.getRandom())
                .ifPresent(attributeInfo -> {
                    AttributeInstance instance = livingEntity.getAttribute(attributeInfo.attribute());
                    if (instance != null) {
                        UUID uuid = attributeInfo.attributeModifier().getId();
                        if (instance.getModifier(uuid) != null){
                            instance.removeModifier(uuid);
                        }
                        instance.addTransientModifier(attributeInfo.attributeModifier());
                    }
                });
    }

    public static class Builder {
        private List<WeightedEntry.Wrapper<AttributeWeightedModule.AttributeInfo>> attributes = Lists.newArrayList();

        public Builder add(AttributeWeightedModule.AttributeInfo instance, int weight){
            attributes.add(WeightedEntry.wrap(instance,weight));
            return this;
        }
        public AttributePredicate build(){
            return new AttributePredicate(new AttributeWeightedModule(attributes));
        }
    }
}
