package com.pancake.surviving_the_aftermath.common.capability;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AftermathCap implements INBTSerializable<CompoundTag> {
    private static final AftermathManager AFTERMATH_MANAGER = AftermathManager.getInstance();
    private final Level level;

    public AftermathCap(Level level) { this.level = level;}

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        AFTERMATH_MANAGER.getAftermathMap().forEach((uuid, aftermath) -> {
            IAftermath.CODEC.get().encodeStart(NbtOps.INSTANCE, aftermath)
                    .resultOrPartial(SurvivingTheAftermath.LOGGER::error)
                    .ifPresent(tag -> {
                        compoundTag.put(uuid.toString(), tag);
                    });
        });
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

        public Provider(Level level) {
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