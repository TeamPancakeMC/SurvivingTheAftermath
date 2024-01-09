package com.pancake.surviving_the_aftermath.api.module;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.IModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.function.Supplier;

public interface IEntityInfoModule extends IModule<IEntityInfoModule> {
    Supplier<Codec<IEntityInfoModule>> CODEC = () -> ModuleRegistry.ENTITY_INFO_REGISTRY.get().getCodec()
            .dispatch("entity_info", IEntityInfoModule::type, IEntityInfoModule::codec);

    List<LazyOptional<Entity>> spawnEntity(ServerLevel level);
}
