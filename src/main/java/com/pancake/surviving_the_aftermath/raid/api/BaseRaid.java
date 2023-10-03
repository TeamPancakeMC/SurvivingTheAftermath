package com.pancake.surviving_the_aftermath.raid.api;

import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.api.BaseAftermath;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public abstract class BaseRaid extends BaseAftermath implements IRaid{
    protected BlockPos centerPos;

    public BaseRaid(UUID uuid, ServerLevel serverLevel, BlockPos centerPos) {
        super(uuid, serverLevel);
        this.centerPos = centerPos;
    }

    public BaseRaid(CompoundTag compoundTag, ServerLevel level) {
        super(compoundTag, level);
    }

    @Override
    public boolean isCreate() {
        return noRaidAt(centerPos, getRadius());
    }

    @Override
    public int getRadius() {
        return 50;
    }
    @Override
    public BlockPos getCenterPos() {
        return centerPos;
    }
    private boolean noRaidAt(BlockPos pos, int radius) {
        return AftermathManager.getInstance().getAftermathMap().values().stream().noneMatch(aftermath -> {
            if (aftermath instanceof IRaid raid) {
                return raid.getCenterPos().distSqr(pos) < radius * radius;
            }
            return false;
        });
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        super.deserializeNBT(compoundTag);
        this.centerPos = NbtUtils.readBlockPos(compoundTag.getCompound("centerPos"));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        compoundTag.put("centerPos", NbtUtils.writeBlockPos(centerPos));
        return compoundTag;
    }
}
