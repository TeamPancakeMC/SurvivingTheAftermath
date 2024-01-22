package com.pancake.surviving_the_aftermath.common.module.entity_info;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.IPredicateModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;


public class EntityInfoWithPredicateModule extends EntityInfoModule {
    public static final String IDENTIFIER = "entity_info_predicate";
    public static final Codec<EntityInfoWithPredicateModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            BuiltInRegistries.ENTITY_TYPE.byNameCodec().fieldOf("entity_type").forGetter(EntityInfoModule::getEntityType),
            IAmountModule.CODEC.get().fieldOf("amount_module").forGetter(EntityInfoModule::getAmountModule),
            Codec.list(IPredicateModule.CODEC.get()).fieldOf("predicate_modules").forGetter(EntityInfoWithPredicateModule::getPredicateModules)
    ).apply(instance, EntityInfoWithPredicateModule::new));
    private List<IPredicateModule> predicateModules = Lists.newArrayList();

    private List<IPredicateModule> getPredicateModules() {
        return predicateModules;
    }

    public EntityInfoWithPredicateModule(EntityType<?> entityType, IAmountModule amountModule, List<IPredicateModule> predicateModules) {
        super(entityType, amountModule);
        this.predicateModules = predicateModules;
    }

    public EntityInfoWithPredicateModule() {
    }

    @Override
    public List<LazyOptional<Entity>> spawnEntity(Level level) {
        List<LazyOptional<Entity>> arrayList = super.spawnEntity(level);
        arrayList.forEach(lazyOptional -> lazyOptional.ifPresent(entity -> {
            if (entity instanceof LivingEntity livingEntity){
                if (predicateModules.isEmpty()) return;
                predicateModules.forEach(predicateModule -> predicateModule.apply(livingEntity));
            }
        }));
        return arrayList;
    }
    @Override
    public Codec<? extends IEntityInfoModule> codec() {
        return CODEC;
    }

    @Override
    public IEntityInfoModule type() {
        return ModAftermathModule.ENTITY_INFO_PREDICATE.get();
    }


    public static class Builder extends EntityInfoModule.Builder{
        private final List<IPredicateModule> predicateModules = Lists.newArrayList();

        public Builder(String entityType) {
            super(entityType);
        }
        public Builder(EntityType<?> entityType) {
            super(entityType);
        }

        public Builder add(IPredicateModule predicate){
            predicateModules.add(predicate);
            return this;
        }

        public EntityInfoModule build() {
            return new EntityInfoWithPredicateModule(entityType,amountModule,predicateModules);
        }
    }
}
