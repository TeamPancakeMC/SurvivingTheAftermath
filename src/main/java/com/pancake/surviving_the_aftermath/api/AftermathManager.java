package com.pancake.surviving_the_aftermath.api;

import com.google.common.collect.Maps;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AftermathManager {
    private final Map<UUID, IAftermath<IAftermathModule>> AFTERMATH_MAP = Maps.newHashMap();
    private static final AftermathManager INSTANCE = new AftermathManager();
    public static AftermathManager getInstance() { return INSTANCE; }
    private AftermathManager() {}

    public void tick() {
        Iterator<IAftermath<IAftermathModule>> iterator = AFTERMATH_MAP.values().iterator();
        while (iterator.hasNext()) {
            IAftermath<? extends IAftermathModule> aftermath = iterator.next();
            if (aftermath.isEnd() || aftermath.isLose()) {
                iterator.remove(); // 通过迭代器安全地移除元素
            } else {
                aftermath.tick();
            }
        }
    }

    private void remove(IAftermath<IAftermathModule> aftermath) {
        AFTERMATH_MAP.remove(aftermath.getUUID());
    }

    private void add(IAftermath<IAftermathModule> aftermath) {
        AFTERMATH_MAP.put(aftermath.getUUID(), aftermath);
    }

    public Map<UUID, IAftermath<IAftermathModule>> getAftermathMap() {
        return AFTERMATH_MAP;
    }

    public Optional<IAftermath<IAftermathModule>> getAftermath(UUID uuid) {
        return Optional.ofNullable(AFTERMATH_MAP.get(uuid));
    }

    public boolean create(IAftermath<IAftermathModule> aftermath, Level level, BlockPos pos, @Nullable Player player) {
        if (aftermath.isCreate(level, pos, player)) {
            add(aftermath);
            return true;
        }
        return false;
    }

    public void create(Level level, CompoundTag compoundTag) {
        IAftermath.CODEC.get().parse(NbtOps.INSTANCE,compoundTag)
                .resultOrPartial(SurvivingTheAftermath.LOGGER::error)
                .ifPresent(aftermath -> {
                    aftermath.setLevel(level);
                    add(aftermath);
                });
    }
}
