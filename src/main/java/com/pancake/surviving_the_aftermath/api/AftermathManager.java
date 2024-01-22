package com.pancake.surviving_the_aftermath.api;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class AftermathManager {
    private final Map<UUID, IAftermath> AFTERMATH_MAP = Maps.newHashMap();
    public final Multimap<ResourceLocation, IAftermathModule> AFTERMATH_MODULE_MAP = ArrayListMultimap.create();
    private static final AftermathManager INSTANCE = new AftermathManager();
    public static AftermathManager getInstance() { return INSTANCE; }
    private AftermathManager() {}

    public void tick() {
        Iterator<IAftermath> iterator = AFTERMATH_MAP.values().iterator();
        while (iterator.hasNext()) {
            IAftermath aftermath = iterator.next();
            if (aftermath.isEnd()) {
                iterator.remove();
                aftermath.getTrackers().forEach(ITracker::unregister);
            } else {
                aftermath.tick();
            }
        }
    }



    private void add(IAftermath aftermath) {
        AFTERMATH_MAP.put(aftermath.getUUID(), aftermath);
        aftermath.getTrackers().forEach(ITracker::register);
    }

    public Map<UUID, IAftermath> getAftermathMap() {
        return AFTERMATH_MAP;
    }

    public Optional<IAftermath> getAftermath(UUID uuid) {
        return Optional.ofNullable(AFTERMATH_MAP.get(uuid));
    }

    public boolean create(IAftermath aftermath, Level level, BlockPos pos, @Nullable ServerPlayer player) {
        if (aftermath.isCreate(level, pos, player)) {
            add(aftermath.Create());
            return true;
        }
        return false;
    }

    public void create(ServerLevel level, CompoundTag compoundTag) {
        IAftermath.CODEC.get().parse(NbtOps.INSTANCE,compoundTag)
                .resultOrPartial(SurvivingTheAftermath.LOGGER::error)
                .ifPresent(aftermath -> {
                    aftermath.setLevel(level);
                    add(aftermath);
                });
    }

    public Multimap<ResourceLocation, IAftermathModule> getAftermathModuleMap() {
        return AFTERMATH_MODULE_MAP;
    }

    public void fillAftermathModuleMap(Multimap<ResourceLocation, IAftermathModule> map) {
        AFTERMATH_MODULE_MAP.putAll(map);
    }

}
