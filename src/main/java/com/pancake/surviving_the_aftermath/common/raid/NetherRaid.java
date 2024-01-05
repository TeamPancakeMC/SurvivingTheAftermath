package com.pancake.surviving_the_aftermath.common.raid;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import net.minecraft.core.BlockPos;

public class NetherRaid extends BaseRaid<NetherRaidModule> {
    public static final String IDENTIFIER = "nether_raid";

    @Override
    public Codec<? extends IAftermath<BaseAftermathModule>> codec() {
        return null;
    }

    @Override
    public IAftermath<BaseAftermathModule> type() {
        return null;
    }

    @Override
    public String getUniqueIdentifier() {
        return null;
    }

    @Override
    public BlockPos getCenterPos() {
        return null;
    }
}