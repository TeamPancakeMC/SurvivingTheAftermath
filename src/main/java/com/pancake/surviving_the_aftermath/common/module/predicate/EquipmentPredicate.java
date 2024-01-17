package com.pancake.surviving_the_aftermath.common.module.predicate;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IPredicateModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;

public class EquipmentPredicate implements IPredicateModule {
    public static final String IDENTIFIER = "equipment";
    public static final Codec<EquipmentPredicate> CODEC = ItemWeightedModule.CODEC.xmap(EquipmentPredicate::new, EquipmentPredicate::getEquipment);
    public ItemWeightedModule equipment;

    public EquipmentPredicate(ItemWeightedModule equipment) {
        this.equipment = equipment;
    }

    public EquipmentPredicate() {
    }

    public ItemWeightedModule getEquipment() {
        return equipment;
    }

    @Override
    public Codec<? extends IPredicateModule> codec() {
        return CODEC;
    }

    @Override
    public IPredicateModule type() {
        return ModAftermathModule.EQUIPMENT_PREDICATE.get();
    }
}
