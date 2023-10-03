package com.pancake.surviving_the_aftermath.raid;

import com.pancake.surviving_the_aftermath.raid.api.BaseRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import java.util.UUID;

public class NetherRaid extends BaseRaid {
    public static final String IDENTIFIER = "NetherRaid";


    public NetherRaid(UUID uuid, ServerLevel serverLevel, BlockPos centerPos) {
        super(uuid, serverLevel, centerPos);
    }

    public NetherRaid(CompoundTag compoundTag, ServerLevel level) {
        super(compoundTag, level);
    }

    @Override
    public void tick() {
        super.tick();
        //TODO: 地狱突袭的逻辑
    }

    @Override
    public void spawn(ServerLevel level, BlockPos spawnPos) {

    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }
}
