package com.pancake.surviving_the_aftermath.api.module;


import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.IModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface IAftermathModule extends IModule<IAftermathModule> {
    Supplier<Codec<IAftermathModule>> CODEC = () -> ModuleRegistry.AFTERMATH_MODULE_REGISTRY.get().getCodec()
            .dispatch("aftermath_module", IAftermathModule::type, IAftermathModule::codec);

    boolean isCreate(Level level, BlockPos pos, @Nullable Player player);
    ResourceLocation getRegistryName();
    String getModuleName();
}
