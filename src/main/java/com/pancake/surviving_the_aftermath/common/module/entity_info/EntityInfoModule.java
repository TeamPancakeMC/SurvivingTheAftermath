package com.pancake.surviving_the_aftermath.common.module.entity_info;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.util.RegistryUtil;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;

public class EntityInfoModule implements IEntityInfoModule {
    public static final String IDENTIFIER = "entity_info";
    public static final Codec<EntityInfoModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfoModule::getEntityType),
            IAmountModule.CODEC.get().fieldOf("amount_module").forGetter(EntityInfoModule::getAmountModule)

    ).apply(instance, EntityInfoModule::new));
    protected EntityType<?> entityType;
    protected IAmountModule amountModule;

    public EntityInfoModule(EntityType<?> entityType, IAmountModule amountModule) {
        this.entityType = entityType;
        this.amountModule = amountModule;
    }

    public EntityInfoModule() {
    }

    @Override
    public List<LazyOptional<Entity>> spawnEntity(Level level) {
        List<LazyOptional<Entity>> arrayList = Lists.newArrayList();
        for (int i = 0; i < amountModule.getSpawnAmount(); i++) {
            Entity entity = entityType.create(level);
            arrayList.add(entity == null ? LazyOptional.empty() : LazyOptional.of(() -> entity));
        }
        return arrayList;
    }
    public EntityType<?> getEntityType() {
        return entityType;
    }

    public IAmountModule getAmountModule() {
        return amountModule;
    }

    @Override
    public Codec<? extends IEntityInfoModule> codec() {
        return CODEC;
    }

    @Override
    public IEntityInfoModule type() {
        return ModAftermathModule.ENTITY_INFO.get();
    }

    public static class Builder {
        protected final EntityType<?> entityType;
        protected IAmountModule amountModule;

        public Builder(String entityType) {
            this.entityType = RegistryUtil.getEntityTypeFromRegistryName(entityType);
        }

        public Builder(EntityType<?> entityType) {
            this.entityType = entityType;
        }

        public Builder amountModule(IAmountModule amountModule) {
            this.amountModule = amountModule;
            return this;
        }
        public EntityInfoModule build() {
            return new EntityInfoModule(entityType, amountModule);
        }
    }
}
