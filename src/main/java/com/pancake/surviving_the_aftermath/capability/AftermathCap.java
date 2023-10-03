package com.pancake.surviving_the_aftermath.capability;

import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.init.ModCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

public class AftermathCap implements INBTSerializable<CompoundTag> {
    private static final AftermathManager AFTERMATH_MANAGER = AftermathManager.getInstance();
    private final ServerLevel level;

    public AftermathCap(ServerLevel level) {
        this.level = level;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        AFTERMATH_MANAGER.getAftermathMap().forEach((uuid, aftermath) -> compoundTag.put(uuid.toString(), aftermath.serializeNBT()));
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        nbt.getAllKeys().forEach(key -> AFTERMATH_MANAGER.create(level, nbt.getCompound(key)));
    }
    public static LazyOptional<AftermathCap> get(Level level) {
        return level.getCapability(ModCapability.AFTERMATH_CAP);
    }

    public void tick() {
        AFTERMATH_MANAGER.tick();
    }

    public static class Provider implements ICapabilitySerializable<CompoundTag> {

        private final LazyOptional<AftermathCap> instance;

        public Provider(ServerLevel level) {
            instance = LazyOptional.of(() -> new AftermathCap(level));
        }

        @Override
        @NotNull
        public <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return ModCapability.AFTERMATH_CAP.orEmpty(cap, instance);
        }

        @Override
        public CompoundTag serializeNBT() {
            return instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")).serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")).deserializeNBT(nbt);
        }

    }
}
