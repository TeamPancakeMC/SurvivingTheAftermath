package com.pancake.surviving_the_aftermath.common.module.predicate;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IPredicateModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.AttributeWeightedModule;

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
}
