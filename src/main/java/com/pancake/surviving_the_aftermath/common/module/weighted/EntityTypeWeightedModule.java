package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.util.RegistryUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;

import java.util.List;

public class EntityTypeWeightedModule extends BaseWeightedModule<EntityType<?>> {
    public static final String IDENTIFIER = "entity_type_weighted";

    public static final Codec<EntityTypeWeightedModule> CODEC = Codec.list(WeightedEntry.Wrapper.codec(BuiltInRegistries.ENTITY_TYPE.byNameCodec()))
            .xmap(EntityTypeWeightedModule::new, EntityTypeWeightedModule::getList);

    public EntityTypeWeightedModule(List<WeightedEntry.Wrapper<EntityType<?>>> list) {
        super(list);
    }

    public EntityTypeWeightedModule() {
    }
    @Override
    public Codec<? extends IWeightedModule<EntityType<?>>> codec() {
        return CODEC;
    }

    @Override
    public IWeightedModule<EntityType<?>> type() {
        return ModAftermathModule.ENTITY_TYPE_WEIGHTED.get();
    }

    public static class Builder {
        private List<WeightedEntry.Wrapper<EntityType<?>>> list;
        public Builder add(String entityType, int weight) {
            this.list.add(WeightedEntry.wrap(RegistryUtil.getEntityTypeFromRegistryName(entityType), weight));
            return this;
        }
        public EntityTypeWeightedModule build() {
            return new EntityTypeWeightedModule(list);
        }
    }
}
