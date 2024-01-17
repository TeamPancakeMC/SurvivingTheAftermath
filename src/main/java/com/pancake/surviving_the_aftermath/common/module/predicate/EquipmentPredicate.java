package com.pancake.surviving_the_aftermath.common.module.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IPredicateModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;

public class EquipmentPredicate implements IPredicateModule {
    public static final String IDENTIFIER = "equipment";
    public static final Codec<EquipmentPredicate> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            ItemWeightedModule.CODEC.fieldOf("equipment").forGetter(EquipmentPredicate::getEquipment),
            Codec.BOOL.fieldOf("can_drop").forGetter(EquipmentPredicate::canDrop)
    ).apply(instance, EquipmentPredicate::new));


    public ItemWeightedModule equipment;
    public boolean canDrop;

    public EquipmentPredicate(ItemWeightedModule equipment, boolean canDrop) {
        this.equipment = equipment;
        this.canDrop = canDrop;
    }

    public EquipmentPredicate() {
    }

    public ItemWeightedModule getEquipment() {
        return equipment;
    }
    public Boolean canDrop() {
        return canDrop;
    }

    @Override
    public Codec<? extends IPredicateModule> codec() {
        return CODEC;
    }

    @Override
    public IPredicateModule type() {
        return ModAftermathModule.EQUIPMENT_PREDICATE.get();
    }

    @Override
    public void apply(LivingEntity livingEntity) {
        if (livingEntity instanceof Mob mob){
            if (!canDrop){
                for (var slot : EquipmentSlot.values()) {
                    mob.setDropChance(slot, 0);
                }
            }

            equipment.getWeightedList().getRandomValue(livingEntity.getRandom())
                    .ifPresent(item -> {
                        mob.equipItemIfPossible(item.getDefaultInstance());
                    });
        }


    }
}
