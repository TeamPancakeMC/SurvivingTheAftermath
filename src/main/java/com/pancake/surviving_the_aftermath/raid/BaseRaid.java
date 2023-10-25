package com.pancake.surviving_the_aftermath.raid;


import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.tracker.PlayerBattleTracker;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

public abstract class BaseRaid extends BaseAftermath {
    public BaseRaid(ServerLevel level) {
        super(level);
    }

    public BaseRaid(ServerLevel level, CompoundTag compoundTag) {
        super(level, compoundTag);
    }

    @Override
    public void bindTrackers() {
        addTracker(new PlayerBattleTracker());
    }
}
