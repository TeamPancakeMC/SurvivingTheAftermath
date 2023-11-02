package com.pancake.surviving_the_aftermath.api.aftermath;

import com.google.common.collect.Maps;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermathFactory;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.UUID;

public class AftermathManager {
    private final Map<UUID, IAftermath<BaseAftermathModule>> AFTERMATH_MAP = Maps.newHashMap();
    private static final AftermathManager INSTANCE = new AftermathManager();
    public static AftermathManager getInstance() { return INSTANCE; }
    private AftermathManager() {}

    public void tick() {
        for (IAftermath<BaseAftermathModule> raid : AFTERMATH_MAP.values()) {
            if (raid.isEnd()) {
                remove(raid);
            } else {
                raid.tick();
            }
        }
    }

    private void remove(IAftermath<BaseAftermathModule> aftermath) {
        AFTERMATH_MAP.remove(aftermath.getUUID());
        aftermath.getTrackers().forEach(ITracker::unregister);
    }

    private void add(IAftermath<BaseAftermathModule> aftermath) {
        AFTERMATH_MAP.put(aftermath.getUUID(), aftermath);
        aftermath.getTrackers().forEach(ITracker::register);
    }

    public Map<UUID, IAftermath<BaseAftermathModule>> getAftermathMap() {
        return AFTERMATH_MAP;
    }

    public boolean create(IAftermath<BaseAftermathModule> aftermath) {
        if (aftermath.isCreate()) {
            add(aftermath);
            return true;
        }
        return false;
    }

    public void create(ServerLevel level, CompoundTag compoundTag) {
        IAftermathFactory aftermathFactory = AftermathAPI.getInstance().getAftermathFactory(compoundTag.getString("identifier"));
        IAftermath aftermath = aftermathFactory.create(level, compoundTag);
        aftermath.deserializeNBT(compoundTag);
        add(aftermath);
    }
}
