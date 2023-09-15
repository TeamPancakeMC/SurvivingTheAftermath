package mod.surviving_the_aftermath.raid;

import com.google.common.collect.Maps;
import mod.surviving_the_aftermath.event.battle_tracker.MobBattleTracker;
import mod.surviving_the_aftermath.event.battle_tracker.PlayerBattleTracker;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.MinecraftForge;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class RaidManager {
    private static final RaidManager INSTANCE = new RaidManager();
    private static final Map<UUID, IRaid> raids = Maps.newHashMap();
    private static final Map<UUID, PlayerBattleTracker> playerBattleTrackers = Maps.newHashMap();
    private static final Map<UUID, MobBattleTracker> mobBattleTrackers = Maps.newHashMap();

    private RaidManager() {
    }

    public static RaidManager getInstance() {
        return INSTANCE;
    }


    public void create(IRaid raid,ServerLevel level) {
        if (noRaidAt(raid.getCenterPos(), raid.getRadius())) {
            raid.create(level);
            add(raid);
        }
    }

    public void create(ServerLevel level, CompoundTag compound) {
        IRaid raid = RaidFactory.create(level, compound);
        if (raid != null) {
            add(raid);
        }
    }
    public void add(IRaid raid) {
        UUID uuid = raid.getUUID();
        raids.put(uuid, raid);
        playerBattleTrackers.put(uuid, new PlayerBattleTracker(uuid));
        mobBattleTrackers.put(uuid, new MobBattleTracker(uuid));
        registerBattleTrackers(uuid);
    }

    public void remove(UUID raidId) {
        raids.remove(raidId);
        playerBattleTrackers.remove(raidId);
        mobBattleTrackers.remove(raidId);
        unregisterBattleTrackers(raidId);
    }

    public Optional<IRaid> getRaid(UUID raidId) {
        return Optional.ofNullable(raids.get(raidId));
    }

    public PlayerBattleTracker getPlayerBattleTracker(UUID raidId) {
        return playerBattleTrackers.get(raidId);
    }

    public MobBattleTracker getMobBattleTracker(UUID raidId) {
        return mobBattleTrackers.get(raidId);
    }

    public Map<UUID, IRaid> getRaids() {
        return raids;
    }

    public Map<UUID, PlayerBattleTracker> getPlayerBattleTrackers() {
        return playerBattleTrackers;
    }

    public Map<UUID, MobBattleTracker> getMobBattleTrackers() {
        return mobBattleTrackers;
    }

    public void unregisterBattleTrackers(UUID raidId) {
        PlayerBattleTracker playerBattleTracker = playerBattleTrackers.get(raidId);
        MobBattleTracker mobBattleTracker = mobBattleTrackers.get(raidId);
        if (playerBattleTracker == null || mobBattleTracker == null) {
            return;
        }
        MinecraftForge.EVENT_BUS.unregister(playerBattleTracker);
        MinecraftForge.EVENT_BUS.unregister(mobBattleTracker);
    }

    public void registerBattleTrackers(UUID raidId) {
        MinecraftForge.EVENT_BUS.register(playerBattleTrackers.get(raidId));
        MinecraftForge.EVENT_BUS.register(mobBattleTrackers.get(raidId));
    }

    public void clear() {
        raids.clear();
        playerBattleTrackers.clear();
        mobBattleTrackers.clear();
    }


    private boolean noRaidAt(BlockPos pos, int radius) {
        return raids.values().stream().noneMatch(raid -> raid.getCenterPos().distSqr(pos) < radius * radius);
    }

    public void tick(ServerLevel level) {
        for (IRaid raid : raids.values()) {
            raid.tick(level);
            if (raid.loseOrEnd()) {
                remove(raid.getUUID());
            }
        }
    }

    public void joinRaid(Entity entity) {
        raids.values().forEach(raid -> raid.join(entity));
    }
}
