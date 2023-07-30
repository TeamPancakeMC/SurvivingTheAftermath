package mod.surviving_the_aftermath.event;


import mod.surviving_the_aftermath.init.ModCapability;
import mod.surviving_the_aftermath.init.ModMobEffects;
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
    private List<UUID> deathList = Lists.newArrayList();

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
                player.getCapability(ModCapability.PLAYER_BATTLE).ifPresent(cap -> cap.setLastEscapeTime(level.getGameTime()));
            }else {
                deathList.add(uuid);
            }
        }

        for (UUID uuid : eventPlayers) {
            if (!players.contains(uuid)) {
                Player player = level.getPlayerByUUID(uuid);
                if (player != null){
                    player.getCapability(ModCapability.PLAYER_BATTLE).ifPresent(cap -> cap.setLastEscapeTime(0L));
                    players.add(uuid);
                }
            }
        }
    }

    //玩家复活
    @SubscribeEvent
    public void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        Player player = event.getEntity();
        Level level = player.level();
        if (level.isClientSide) return;
        if (player instanceof ServerPlayer serverPlayer) {
            if (deathList.contains(serverPlayer.getUUID())) {
                deathList.remove(serverPlayer.getUUID());
                serverPlayer.getCapability(ModCapability.PLAYER_BATTLE).ifPresent(cap ->{
                    System.out.println(players.size());
                    if (players.size() > 0){
                        player.displayClientMessage(Component.translatable(PLAYER_BATTLE_PERSONAL_FAIL), true);
                        serverPlayer.setGameMode(GameType.SPECTATOR);
                        return;
                    }

                    int deathCount = cap.getDeathCount();
                    if (deathCount < MAX_DEATH_COUNT) {
                        //TODO:游戏结束
                    }
                    cap.setDeathCount(deathCount + 1);
                });
            }
        }
    }

    @SubscribeEvent
    public void onTickPlayerTick(TickEvent.PlayerTickEvent event) {
        Player player = event.player;
        Level level = player.level();
        if (level.isClientSide()) return;
        player.getCapability(ModCapability.PLAYER_BATTLE).ifPresent(cap ->{
            Long lastEscapeTime = cap.getLastEscapeTime();
            long time = level.getGameTime() - lastEscapeTime;
            if (lastEscapeTime != 0L && time > 20 * 5) {
                player.addEffect(new MobEffectInstance(ModMobEffects.COWARDICE.get(), 45 * 60 * 20));
            }
            if(lastEscapeTime != 0L && time < 20 * 5){
                player.displayClientMessage(Component.translatable(PLAYER_BATTLE_ESCAPE, 20 * 5 - time), true);
            }
        });
    }
}
