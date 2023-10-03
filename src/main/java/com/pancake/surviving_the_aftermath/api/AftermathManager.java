package com.pancake.surviving_the_aftermath.api;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;

import java.util.Map;
import java.util.UUID;

public class AftermathManager {
    private final Map<UUID,IAftermath> AFTERMATH_MAP = Maps.newHashMap();

    private static final AftermathManager INSTANCE = new AftermathManager();

    public static AftermathManager getInstance() {
        return INSTANCE;
    }

    private AftermathManager() {}

    public void tick() {
        for (IAftermath raid : AFTERMATH_MAP.values()) {
            raid.tick();
            if (raid.loseOrEnd()) {
                UUID uuid = raid.getUUID();
                remove(uuid);
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

    public void create(ServerLevel level, CompoundTag compound) {
        IAftermathFactory aftermathFactory = AftermathAPI.getInstance().getAftermathFactory(compound.getString("identifier"));
        IAftermath aftermath = aftermathFactory.create(level, compound);
        aftermath.deserializeNBT(compound);
        add(aftermath);
    }
}
