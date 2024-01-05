package com.pancake.surviving_the_aftermath.common.module.entity_info;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;


public class EntityInfoWithEquipmentModule extends EntityInfoModule {
    public static final String IDENTIFIER = "entity_info_equipment";
    public static final Codec<EntityInfoWithEquipmentModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfoModule::getEntityType),
            ModuleRegistry.Codecs.AMOUNT_CODEC.get().fieldOf("amount_module").forGetter(EntityInfoModule::getAmountModule),
            ModuleRegistry.Codecs.WEIGHTED_CODEC.get().fieldOf("equipment").forGetter(EntityInfoWithEquipmentModule::getEquipment)
    ).apply(instance, EntityInfoWithEquipmentModule::new));
    private IWeightedModule<?> equipment;

    public EntityInfoWithEquipmentModule(EntityType<?> entityType, IAmountModule amountModule, IWeightedModule<?> equipment) {
        super(entityType, amountModule);
        this.equipment = equipment;
    }

    public EntityInfoWithEquipmentModule() {
    }

    private IWeightedModule<?> getEquipment() {
        return equipment;
    }

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Codec<? extends IEntityInfoModule> codec() {
        return CODEC;
    }

    @Override
    public IEntityInfoModule type() {
        return ModAftermathModule.ENTITY_INFO_EQUIPMENT.get();
    }

    public static class Builder extends EntityInfoModule.Builder{
        private IWeightedModule<?> equipment;

        public Builder equipment(IWeightedModule<?> equipment) {
            this.equipment = equipment;
            return this;
        }

        public EntityInfoWithEquipmentModule build() {
            EntityInfoModule build = super.build();
            EntityType<?> entityType = build.getEntityType();
            IAmountModule amountModule = build.getAmountModule();
            return new EntityInfoWithEquipmentModule(entityType, amountModule, equipment);
        }
    }
}
