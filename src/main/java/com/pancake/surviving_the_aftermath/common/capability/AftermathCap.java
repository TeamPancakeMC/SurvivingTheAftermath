package com.pancake.surviving_the_aftermath.common.capability;

import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathManager;
import com.pancake.surviving_the_aftermath.common.init.ModCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.UUID;

public class AftermathCap implements INBTSerializable<CompoundTag> {
    private static final AftermathManager AFTERMATH_MANAGER = AftermathManager.getInstance();
    private final ServerLevel level;

    public AftermathCap(ServerLevel level) { this.level = level;}

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        for (Map.Entry<UUID, IAftermath> entry : AFTERMATH_MANAGER.getAftermathMap().entrySet()) {
            UUID uuid = entry.getKey();
            IAftermath aftermath = entry.getValue();
            compoundTag.put(uuid.toString(), aftermath.serializeNBT());
        }
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        for (String uuid : compoundTag.getAllKeys()) {
            CompoundTag tag = compoundTag.getCompound(uuid);
            AFTERMATH_MANAGER.create(level, tag);
        }
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
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
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