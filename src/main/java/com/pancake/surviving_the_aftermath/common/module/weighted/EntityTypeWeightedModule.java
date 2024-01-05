package com.pancake.surviving_the_aftermath.common.module.weighted;

import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IWeightedModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;
import java.util.stream.Collectors;

public class EntityTypeWeightedModule extends BaseWeightedModule<EntityType<?>> {
    public static final String IDENTIFIER = "entity_type_weighted";

    public static final Codec<EntityTypeWeightedModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            WeightedEntry.Wrapper.codec(BuiltInRegistries.ENTITY_TYPE.byNameCodec())
                    .listOf().fieldOf("list").forGetter(EntityTypeWeightedModule::getList)
    ).apply(instance, EntityTypeWeightedModule::new));

    public EntityTypeWeightedModule(List<WeightedEntry.Wrapper<EntityType<?>>> list) {
        super(list);
    }

    public EntityTypeWeightedModule() {
    }

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public Codec<? extends IWeightedModule<EntityType<?>>> codec() {
        return CODEC;
    }

    @Override
    public IWeightedModule<EntityType<?>> type() {
        return ModAftermathModule.ENTITY_TYPE_WEIGHTED.get();
    }
}
