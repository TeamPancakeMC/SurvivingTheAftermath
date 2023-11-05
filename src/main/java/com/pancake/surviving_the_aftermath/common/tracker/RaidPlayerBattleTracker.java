package com.pancake.surviving_the_aftermath.common.tracker;


import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.aftermath.BaseTracker;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.common.init.ModMobEffects;
import com.pancake.surviving_the_aftermath.common.raid.api.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.util.AftermathEventUtil;
import com.pancake.surviving_the_aftermath.common.util.RandomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.stream.Collectors;

public class RaidPlayerBattleTracker extends BaseTracker {
    public static final String IDENTIFIER = "player_battle_tracker";
    public static final String PLAYER_BATTLE_PERSONAL_FAIL = "message.surviving_the_aftermath.tracker.personal_fail";
    public static final String PLAYER_BATTLE_ESCAPE = "message.surviving_the_aftermath.tracker.escape";
    private static final int MAX_DEATH_COUNT = 3;
    private final Set<UUID> players = Sets.newHashSet();
    private final Map<UUID, Integer> deathMap = Maps.newHashMap();
    private final Map<UUID, Long> escapeMap = Maps.newHashMap();
    private final Map<UUID, Set<UUID>> spectatorMap = Maps.newHashMap();
    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @SubscribeEvent
    public void updatePlayer(AftermathEvent.Ongoing event) {
        ServerLevel level = event.getLevel();
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
        System.out.println("玩家数量：" + players);
        System.out.println("死亡玩家数量：" + deathMap);
        System.out.println("逃跑玩家数量：" + escapeMap);
        System.out.println("观战玩家数量：" + spectatorMap);
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (!level.isClientSide && player instanceof ServerPlayer serverPlayer) {
            if (deathMap.containsKey(serverPlayer.getUUID())) {
                Integer deathCount = deathMap.get(serverPlayer.getUUID());

                System.out.println("死亡次数：" + deathCount);
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
                if (aftermath instanceof BaseRaid<?> raid){

                    BlockPos centerPos = raid.getCenterPos();
                    BlockPos pos = player.blockPosition();
                    double distance = Math.sqrt(centerPos.distSqr(pos));

                    System.out.println("玩家逃跑距离：" + distance);


                    if (lastEscapeTime != 0L && time > 20 * 5) {
                        player.addEffect(new MobEffectInstance(ModMobEffects.COWARDICE.get(), 45 * 60 * 20));
                        if (distance > 120) {
                            player.addEffect(new MobEffectInstance(ModMobEffects.COWARDICE.get(), 45 * 60 * 20, 1));
                            escapeMap.remove(uuid);
                            AftermathEventUtil.lose((BaseAftermath<BaseAftermathModule>) aftermath,players, (ServerLevel) level);
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
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString(Constant.IDENTIFIER, getUniqueIdentifier());

//        Set<UUID> players
        ListTag playerTags = new ListTag();
        players.forEach(uuid -> playerTags.add(NbtUtils.createUUID(uuid)));
        compoundTag.put(Constant.PLAYERS, playerTags);


//        Map<UUID, Integer> deathMap
        CompoundTag deathTag = new CompoundTag();
        deathMap.forEach((uuid, deathCount) -> {
            deathTag.putUUID(Constant.UUID, uuid);
            deathTag.putInt(Constant.DEATH_COUNT, deathCount);
        });
        compoundTag.put(Constant.DEATH_MAP, deathTag);

//        Map<UUID, Long> escapeMap
        CompoundTag escapeTag = new CompoundTag();
        escapeMap.forEach((uuid, escapeTime) -> {
            escapeTag.putUUID(Constant.UUID, uuid);
            escapeTag.putLong(Constant.ESCAPE_TIME, escapeTime);
        });
        compoundTag.put(Constant.ESCAPE_MAP, escapeTag);

//        Map<UUID, Set<UUID>> spectatorMap
        CompoundTag spectatorTag = new CompoundTag();
        spectatorMap.forEach((uuid, spectatorUUIDs) -> {
            ListTag spectatorUUIDsTag = new ListTag();
            spectatorUUIDs.forEach(spectatorUUID -> spectatorUUIDsTag.add(NbtUtils.createUUID(spectatorUUID)));
            spectatorTag.put(Constant.UUID, NbtUtils.createUUID(uuid));
            spectatorTag.put(Constant.SPECTATOR_LIST, spectatorUUIDsTag);
        });
        compoundTag.put(Constant.SPECTATOR_MAP, spectatorTag);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        ListTag playerTags = nbt.getList(Constant.PLAYERS, Tag.TAG_INT_ARRAY);
        playerTags.forEach(tag -> players.add(NbtUtils.loadUUID(tag)));

        CompoundTag deathTag = nbt.getCompound(Constant.DEATH_MAP);
        deathTag.getAllKeys().forEach(uuidStr -> {
            UUID uuid = NbtUtils.loadUUID(deathTag.get(uuidStr));
            int deathCount = deathTag.getInt(Constant.DEATH_COUNT);
            deathMap.put(uuid, deathCount);
        });

        CompoundTag escapeTag = nbt.getCompound(Constant.ESCAPE_MAP);
        escapeTag.getAllKeys().forEach(uuidStr -> {
            UUID uuid = NbtUtils.loadUUID(escapeTag.get(uuidStr));
            long escapeTime = escapeTag.getLong(Constant.ESCAPE_TIME);
            escapeMap.put(uuid, escapeTime);
        });

        CompoundTag spectatorTag = nbt.getCompound(Constant.SPECTATOR_MAP);
        spectatorTag.getAllKeys().forEach(uuidStr -> {
            UUID uuid = NbtUtils.loadUUID(spectatorTag.get(uuidStr));
            ListTag spectatorUUIDsTag = spectatorTag.getList(Constant.SPECTATOR_LIST, Tag.TAG_INT_ARRAY);
            Set<UUID> spectatorUUIDs = spectatorUUIDsTag.stream().map(NbtUtils::loadUUID).collect(Collectors.toSet());
            spectatorMap.put(uuid, spectatorUUIDs);
        });
    }
}
