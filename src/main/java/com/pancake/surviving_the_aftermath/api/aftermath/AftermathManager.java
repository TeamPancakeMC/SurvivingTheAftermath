package com.pancake.surviving_the_aftermath.api.aftermath;

import com.google.common.collect.Maps;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;

import java.util.*;

public class AftermathManager {
    private final Map<UUID, IAftermath<IAftermathModule>> AFTERMATH_MAP = Maps.newHashMap();
    private static final AftermathManager INSTANCE = new AftermathManager();
    public static AftermathManager getInstance() { return INSTANCE; }
    private AftermathManager() {}

    public void tick() {
        Iterator<IAftermath<IAftermathModule>> iterator = AFTERMATH_MAP.values().iterator();
        while (iterator.hasNext()) {
            IAftermath<IAftermathModule> raid = iterator.next();
            if (raid.isEnd() || raid.isLose()) {
                iterator.remove(); // 通过迭代器安全地移除元素
            } else {
                raid.tick();
            }
        }
    }

    private void remove(IAftermath<IAftermathModule> aftermath) {
        AFTERMATH_MAP.remove(aftermath.getUUID());
        aftermath.getTrackers().forEach(ITracker::unregister);
    }

    private void add(IAftermath<IAftermathModule> aftermath) {
        AFTERMATH_MAP.put(aftermath.getUUID(), aftermath);
        Set<ITracker> trackers = aftermath.getTrackers();
        trackers.forEach(ITracker::register);
    }

    public Map<UUID, IAftermath<IAftermathModule>> getAftermathMap() {
        return AFTERMATH_MAP;
    }

    public Optional<IAftermath<IAftermathModule>> getAftermath(UUID uuid) {
        return Optional.ofNullable(AFTERMATH_MAP.get(uuid));
    }

    public boolean create(IAftermath<IAftermathModule> aftermath) {
        if (aftermath.isCreate()) {
            add(aftermath);
            return true;
        }
        return false;
    }

    public void create(ServerLevel level, CompoundTag compoundTag) {
        IAftermath.CODEC.get().parse(NbtOps.INSTANCE,compoundTag)
                .resultOrPartial(SurvivingTheAftermath.LOGGER::error)
                .ifPresent(aftermath -> {
                    aftermath.setServerLevel(level);
                    add(aftermath);
                });
    }
}
