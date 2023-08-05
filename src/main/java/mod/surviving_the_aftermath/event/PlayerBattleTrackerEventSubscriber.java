package mod.surviving_the_aftermath.event;

import com.google.common.collect.Maps;
import mod.surviving_the_aftermath.capability.RaidData;
import mod.surviving_the_aftermath.init.ModMobEffects;
import mod.surviving_the_aftermath.raid.NetherRaid;
import mod.surviving_the_aftermath.raid.RaidState;
import net.minecraft.core.BlockPos;
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
import org.apache.commons.compress.utils.Lists;

import java.util.*;

public class PlayerBattleTrackerEventSubscriber {

    public static final String PLAYER_BATTLE_PERSONAL_FAIL = "message.surviving_the_aftermath.nether_raid.personal_fail";
    public static final String PLAYER_BATTLE_ESCAPE = "message.surviving_the_aftermath.nether_raid.escape";
    private static final int MAX_DEATH_COUNT = 3;
    private List<UUID> players = Lists.newArrayList();
    private Map<UUID,Integer> deathMap = Maps.newHashMap();
    private Map<UUID,Long> escapeMap = Maps.newHashMap();
    private Map<UUID, List<UUID>> spectatorMap = new HashMap<>();
    private UUID currentRaidId;

    public PlayerBattleTrackerEventSubscriber(UUID currentRaidId) {
        this.currentRaidId = currentRaidId;
    }

    @SubscribeEvent
    public void updatePlayer(RaidEvent.Ongoing event) {
        ServerLevel level = event.getLevel();
        List<UUID> uuids = new ArrayList<>();
        List<UUID> eventPlayers = event.getPlayers();
        for (UUID uuid : players) {
            if (!eventPlayers.contains(uuid)) {
                uuids.add(uuid);
            }
        }
        for (UUID uuid : uuids) {
            players.remove(uuid);
            Player player = level.getPlayerByUUID(uuid);
            if (player != null){
                escapeMap.put(uuid,level.getGameTime());
            } else {
                deathMap.put(uuid, deathMap.getOrDefault(uuid,0) + 1);
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
                    NetherRaid netherRaid = RaidData.getNetherRaid(currentRaidId);
                    restorePlayerGameMode(level);
                    if (netherRaid != null) netherRaid.setState(RaidState.LOSE);
                }
            }
        }
    }

    @SubscribeEvent
    public void onTickPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        UUID uuid = player.getUUID();

        if (!level.isClientSide && escapeMap.containsKey(uuid)){
            Long lastEscapeTime = escapeMap.get(uuid);
            long time = level.getGameTime() - lastEscapeTime;
            if (lastEscapeTime != 0L && time > 20 * 5) {
                player.addEffect(new MobEffectInstance(ModMobEffects.COWARDICE.get(), 45 * 60 * 20));
            } else {
                player.displayClientMessage(Component.translatable(PLAYER_BATTLE_ESCAPE, 20 * 5 - time), true);
            }
        }
    }

    private void setSpectator(ServerPlayer player,Level level) {
        UUID uuid = players.get(level.random.nextInt(players.size()));
        Player target = level.getPlayerByUUID(uuid);
        if (!(target instanceof ServerPlayer serverTarget)) return;

        if (spectatorMap.containsKey(player.getUUID())){
            List<UUID> uuids = spectatorMap.get(player.getUUID());
            if (uuids != null) {
                uuids.add(player.getUUID());
                for (UUID uuid1 : uuids) {
                    Player player1 = level.getPlayerByUUID(uuid1);
                    if (player1 instanceof ServerPlayer serverPlayer) {
                        if (!spectatorMap.containsKey(serverTarget.getUUID())) {
                            spectatorMap.put(serverTarget.getUUID(), Lists.newArrayList());
                        }
                        spectatorMap.get(serverTarget.getUUID()).add(serverPlayer.getUUID());
                        serverPlayer.setCamera(serverTarget);
                    }
                }
                spectatorMap.remove(player.getUUID());
            }
        } else {
            spectatorMap.put(serverTarget.getUUID(), Lists.newArrayList());
            spectatorMap.get(serverTarget.getUUID()).add(player.getUUID());
            player.setCamera(serverTarget);
        }
        player.setGameMode(GameType.SPECTATOR);
    }

    public void restorePlayerGameMode(Level level) {
        spectatorMap.values().stream()
                .flatMap(Collection::stream)
                .forEach(uuid -> {
                    Player player = level.getPlayerByUUID(uuid);
                    if (player instanceof ServerPlayer serverPlayer){
                        serverPlayer.setGameMode(GameType.SURVIVAL);
                    }
                });
    }

}