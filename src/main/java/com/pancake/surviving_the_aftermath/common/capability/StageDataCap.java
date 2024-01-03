package com.pancake.surviving_the_aftermath.common.capability;

import com.pancake.surviving_the_aftermath.api.stage.LevelStageData;
import com.pancake.surviving_the_aftermath.api.stage.PlayerStageData;
import com.pancake.surviving_the_aftermath.api.stage.StageData;
import com.pancake.surviving_the_aftermath.common.init.ModCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class StageDataCap implements ICapabilitySerializable<CompoundTag> {
    private final LazyOptional<StageDataCap> instance;
    private StageData stageData;

    public StageDataCap(Level level,LevelStageData stageData) {
        this.instance = LazyOptional.of(() -> new StageDataCap(level,stageData));
        this.stageData = stageData;
    }

    public StageDataCap(Player player, PlayerStageData stageData) {
        this.instance = LazyOptional.of(() -> new StageDataCap(player, stageData));
        this.stageData = stageData;
    }


    public static LazyOptional<StageDataCap> get(Level level) {
        return level.getCapability(ModCapability.STAGE_DATA_CAP);
    }

    public static LazyOptional<StageDataCap> get(Player player) {
        return player.getCapability(ModCapability.STAGE_DATA_CAP);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ModCapability.STAGE_DATA_CAP.orEmpty(cap, instance);
    }

    @Override
    public CompoundTag serializeNBT() {
        return stageData.serializeNBT();
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        stageData.deserializeNBT(nbt);
    }

    public StageData getStageData() {
        return stageData;
    }
}
