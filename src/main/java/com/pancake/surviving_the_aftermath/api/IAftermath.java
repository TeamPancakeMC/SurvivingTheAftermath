package com.pancake.surviving_the_aftermath.api;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

public interface IAftermath extends IModule<IAftermath> {
    Supplier<Codec<IAftermath>> CODEC = () -> ModuleRegistry.AFTERMATH_REGISTRY.get().getCodec()
            .dispatch("aftermath", IAftermath::type, IAftermath::codec);
    ResourceLocation getRegistryName();
    ResourceLocation getBarsResource();

    int[] getBarsOffset();

    boolean isCreate(Level level, BlockPos pos, @Nullable Player player);
    void createRewards();

    void updateProgress();

    void tick();

    List<ITracker> getTrackers();

    IAftermathModule getModule();

    boolean isEnd();
    UUID getUUID();

    BlockPos getStartPos();

    void setLevel(ServerLevel level);

    AftermathState getState();

    void setState(AftermathState state);

    void lose();

    Set<UUID> getEnemies();
}
