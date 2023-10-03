package com.pancake.surviving_the_aftermath.raid;

import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermathFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public class NetherRaidFactory implements IAftermathFactory {
    @Override
    public IAftermath create(ServerLevel level, CompoundTag compound) {
        return new NetherRaid(compound,level);
    }

    @Override
    public String getIdentifier() {
        return NetherRaid.IDENTIFIER;
    }
}
