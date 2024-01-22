package com.pancake.surviving_the_aftermath.common.capability;

import com.google.common.collect.Sets;
import com.pancake.surviving_the_aftermath.common.init.ModCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


import java.util.Set;

public class AftermathStageCap implements INBTSerializable<ListTag> {
    private Set<String> stages = Sets.newHashSet();

    @Override
    public ListTag serializeNBT() {
        ListTag listTag = new ListTag();
        for (String stage : stages) {
            listTag.add(StringTag.valueOf(stage));
        }
        return listTag;
    }

    @Override
    public void deserializeNBT(ListTag nbt) {
        for (int i = 0; i < nbt.size(); i++) {
            stages.add(nbt.getString(i));
        }
    }

    public Set<String> getStages() {
        return stages;
    }

    public static void addStage(Player player, String stage) {
        AftermathStageCap.get(player).ifPresent(stageCap -> stageCap.getStages().add(stage));
    }
    public static void addStage(Level level, String stage) {
        AftermathStageCap.get(level).ifPresent(stageCap -> stageCap.getStages().add(stage));
    }

    public static void removeStage(Player player, String stage) {
        AftermathStageCap.get(player).ifPresent(stageCap -> stageCap.getStages().remove(stage));
    }
    public static void removeStage(Level level, String stage) {
        AftermathStageCap.get(level).ifPresent(stageCap -> stageCap.getStages().remove(stage));
    }

    public static boolean hasStage(Player player, String stage) {
        return AftermathStageCap.get(player).map(stageCap -> stageCap.getStages().contains(stage)).orElse(false);
    }
    public static boolean hasStage(Level level, String stage) {
        return AftermathStageCap.get(level).map(stageCap -> stageCap.getStages().contains(stage)).orElse(false);
    }

    public static LazyOptional<AftermathStageCap> get(Player player) {
        return player.getCapability(ModCapability.STAGE_CAP);
    }
    public static LazyOptional<AftermathStageCap> get(Level level) {
        return level.getCapability(ModCapability.STAGE_CAP);
    }

    public static class Provider implements ICapabilitySerializable<ListTag> {
        private final LazyOptional<AftermathStageCap> instance;

        public Provider() {
            instance = LazyOptional.of(AftermathStageCap::new);
        }

        @Override
        public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return ModCapability.STAGE_CAP.orEmpty(cap, instance);
        }

        @Override
        public ListTag serializeNBT() {
            return instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")).serializeNBT();
        }

        @Override
        public void deserializeNBT(ListTag nbt) {
            instance.orElseThrow(() -> new IllegalArgumentException("LazyOptional cannot be empty!")).deserializeNBT(nbt);
        }
    }
}
