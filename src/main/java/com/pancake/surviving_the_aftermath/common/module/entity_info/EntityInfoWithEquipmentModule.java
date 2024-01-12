package com.pancake.surviving_the_aftermath.common.module.entity_info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;


public class EntityInfoWithEquipmentModule extends EntityInfoModule {
    public static final String IDENTIFIER = "entity_info_equipment";
    public static final Codec<EntityInfoWithEquipmentModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfoModule::getEntityType),
            IAmountModule.CODEC.get().fieldOf("amount_module").forGetter(EntityInfoModule::getAmountModule),
            Codec.list(WeightedEntry.Wrapper.codec(BuiltInRegistries.ITEM.byNameCodec()))
                    .xmap(ItemWeightedModule::new, ItemWeightedModule::getList).fieldOf("equipment").forGetter(EntityInfoWithEquipmentModule::getEquipment)
    ).apply(instance, EntityInfoWithEquipmentModule::new));

    private ItemWeightedModule getEquipment() {
        return equipment;
    }

    private ItemWeightedModule equipment;

    public EntityInfoWithEquipmentModule(EntityType<?> entityType, IAmountModule amountModule, ItemWeightedModule equipment) {
        super(entityType, amountModule);
        this.equipment = equipment;
    }

    public EntityInfoWithEquipmentModule() {
    }
    @Override
    public Codec<? extends IEntityInfoModule> codec() {
        return CODEC;
    }

    @Override
    public IEntityInfoModule type() {
        return ModAftermathModule.ENTITY_INFO_EQUIPMENT.get();
    }

}
