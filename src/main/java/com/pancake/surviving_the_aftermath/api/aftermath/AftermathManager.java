package com.pancake.surviving_the_aftermath.api.aftermath;

import com.google.common.collect.Maps;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermathFactory;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.UUID;

public class AftermathManager {
    private final Map<UUID, IAftermath> AFTERMATH_MAP = Maps.newHashMap();
    private static final AftermathManager INSTANCE = new AftermathManager();
    public static AftermathManager getInstance() { return INSTANCE; }
    private AftermathManager() {}

    public void tick() {
        for (IAftermath raid : AFTERMATH_MAP.values()) {
            if (raid.isEnd()) {
                remove(raid.getUUID());
            } else {
                raid.tick();
            }
        }
    }

    private void remove(UUID uuid) {
        AFTERMATH_MAP.remove(uuid);
    }

    private void add(IAftermath aftermath) {
        AFTERMATH_MAP.put(aftermath.getUUID(), aftermath);
    }

    public Map<UUID, IAftermath> getAftermathMap() {
        return AFTERMATH_MAP;
    }

    public void create(IAftermath aftermath) {
        if (aftermath.isCreate()) {
            add(aftermath);
        }
    }

    public void create(ServerLevel level, CompoundTag compoundTag) {
        IAftermathFactory aftermathFactory = AftermathAPI.getInstance().getAftermathFactory(compoundTag.getString("identifier"));
        IAftermath aftermath = aftermathFactory.create(level, compoundTag);
        aftermath.deserializeNBT(compoundTag);
        add(aftermath);
    }
}
