package com.pancake.surviving_the_aftermath.common.event.tracker;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.base.BaseTracker;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModMobEffects;
import com.pancake.surviving_the_aftermath.common.raid.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaid;
import com.pancake.surviving_the_aftermath.common.util.CodecUtils;
import com.pancake.surviving_the_aftermath.common.util.RandomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public class RaidPlayerBattleTracker extends BaseTracker {
    public static final Codec<RaidPlayerBattleTracker> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtils.setOf(CodecUtils.UUID_CODEC).fieldOf("players").forGetter(RaidPlayerBattleTracker::getPlayers),
            Codec.unboundedMap(CodecUtils.UUID_CODEC, Codec.INT).fieldOf("death_map").forGetter(RaidPlayerBattleTracker::getDeathMap),
            Codec.unboundedMap(CodecUtils.UUID_CODEC, Codec.LONG).fieldOf("escape_map").forGetter(RaidPlayerBattleTracker::getEscapeMap),
            Codec.unboundedMap(CodecUtils.UUID_CODEC, CodecUtils.setOf(CodecUtils.UUID_CODEC)).fieldOf("spectator_map").forGetter(RaidPlayerBattleTracker::getSpectatorMap)
    ).apply(instance, RaidPlayerBattleTracker::new));

    public RaidPlayerBattleTracker(Set<UUID> players, Map<UUID, Integer> deathMap, Map<UUID, Long> escapeMap, Map<UUID, Set<UUID>> spectatorMap) {
        this.players = players;
        this.deathMap = deathMap;
        this.escapeMap = escapeMap;
        this.spectatorMap = spectatorMap;
    }

    public RaidPlayerBattleTracker() {
    }

    public static final String IDENTIFIER = "player_battle_tracker";
    public static final String PLAYER_BATTLE_PERSONAL_FAIL = "message.surviving_the_aftermath.tracker.personal_fail";
    public static final String PLAYER_BATTLE_ESCAPE = "message.surviving_the_aftermath.tracker.escape";
    private static final int MAX_DEATH_COUNT = 3;
    private Set<UUID> players = Sets.newLinkedHashSet();
    private Map<UUID, Integer> deathMap = Maps.newHashMap();
    private Map<UUID, Long> escapeMap = Maps.newHashMap();
    private Map<UUID, Set<UUID>> spectatorMap = Maps.newHashMap();

    @SubscribeEvent
    public void updatePlayer(AftermathEvent.Ongoing event) {
        Level level = event.getLevel();
        Set<UUID> uuids = Sets.newHashSet();
        Set<UUID> eventPlayers = event.getPlayers();
        for (UUID uuid : players) {
            if (!eventPlayers.contains(uuid)) {
                uuids.add(uuid);
            }
        }
        for (UUID uuid : uuids) {
            players.remove(uuid);
            Player player = level.getPlayerByUUID(uuid);
            if (player != null) {
                escapeMap.put(uuid, level.getGameTime());
            } else {
                deathMap.put(uuid, deathMap.getOrDefault(uuid, 0) + 1);
            }
        }

        for (UUID uuid : eventPlayers) {
            if (!players.contains(uuid)) {
                Player player = level.getPlayerByUUID(uuid);
                if (player instanceof ServerPlayer serverPlayer && serverPlayer.gameMode.getGameModeForPlayer() != GameType.SPECTATOR) {
                    escapeMap.remove(uuid);
                    players.add(uuid);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (deathMap.containsKey(serverPlayer.getUUID())) {
                Integer deathCount = deathMap.get(serverPlayer.getUUID());

                if (!players.isEmpty()) {
                    player.displayClientMessage(Component.translatable(PLAYER_BATTLE_PERSONAL_FAIL), true);
                    setSpectator(serverPlayer, level);
                    deathMap.remove(serverPlayer.getUUID());
                    return;
                }

                if (deathCount >= MAX_DEATH_COUNT || spectatorMap.containsKey(serverPlayer.getUUID())) {
                    restorePlayerGameMode(level);
                    manager.getAftermath(uuid).ifPresent(IAftermath::lose);
                }
            }
        }
    }




    @SubscribeEvent
    public void onPlayerEscape(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        UUID uuidPlayer = player.getUUID();

        if (!level.isClientSide && escapeMap.containsKey(uuidPlayer)) {
            Long lastEscapeTime = escapeMap.get(uuidPlayer);
            long time = level.getGameTime() - lastEscapeTime;

            manager.getAftermath(uuid).ifPresent(aftermath -> {
                if (aftermath instanceof IRaid raid){

                    BlockPos centerPos = raid.getStartPos();
                    BlockPos pos = player.blockPosition();
                    double distance = Math.sqrt(centerPos.distSqr(pos));


                    if (lastEscapeTime != 0L && time > 20 * 5) {
                        player.addEffect(new MobEffectInstance(ModMobEffects.COWARDICE.get(), 45 * 60 * 20));
                        if (distance > 120) {
                            player.addEffect(new MobEffectInstance(ModMobEffects.COWARDICE.get(), 45 * 60 * 20, 1));
                            escapeMap.remove(uuid);
                            restorePlayerGameMode(level);
                        }
                    } else {
                        player.displayClientMessage(Component.translatable(PLAYER_BATTLE_ESCAPE, 20 * 5 - time), true);
                    }
                }
            });
        }
    }

    public void restorePlayerGameMode(Level level) {
        spectatorMap.values().stream()
                .flatMap(Collection::stream)
                .map(level::getPlayerByUUID)
                .filter(player -> player instanceof ServerPlayer)
                .map(player -> (ServerPlayer) player)
                .forEach(player -> player.setGameMode(GameType.SURVIVAL));
    }

    private void setSpectator(ServerPlayer player, Level level) {
        Player target = level.getPlayerByUUID(RandomUtils.getRandomElement(players));
        if (!(target instanceof ServerPlayer serverTarget)) return;

        if (spectatorMap.containsKey(player.getUUID())) {
            Set<UUID> uuids = spectatorMap.get(player.getUUID());
            if (uuids != null) {
                uuids.add(player.getUUID());
                for (UUID uuid1 : uuids) {
                    if (level.getPlayerByUUID(uuid1) instanceof ServerPlayer serverPlayer) {
                        if (!spectatorMap.containsKey(serverTarget.getUUID())) {
                            spectatorMap.put(serverTarget.getUUID(), Sets.newHashSet());
                        }
                        spectatorMap.get(serverTarget.getUUID()).add(serverPlayer.getUUID());
                        serverPlayer.setCamera(serverTarget);
                    }
                }
                spectatorMap.remove(player.getUUID());
            }

        } else {
            spectatorMap.put(serverTarget.getUUID(), Sets.newHashSet());
            spectatorMap.get(serverTarget.getUUID()).add(player.getUUID());
            player.setCamera(serverTarget);
        }
        player.setGameMode(GameType.SPECTATOR);
    }

    @Override
    public Codec<? extends ITracker> codec() {
        return CODEC;
    }

    @Override
    public ITracker type() {
        return ModAftermathModule.RAID_PLAYER_BATTLE_TRACKER.get();
    }

    public Set<UUID> getPlayers() {
        return players;
    }

    public Map<UUID, Integer> getDeathMap() {
        return deathMap;
    }

    public Map<UUID, Long> getEscapeMap() {
        return escapeMap;
    }

    public Map<UUID, Set<UUID>> getSpectatorMap() {
        return spectatorMap;
    }
}