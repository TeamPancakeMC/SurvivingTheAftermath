package mod.surviving_the_aftermath;

import mod.surviving_the_aftermath.data.RaidEnemyInfo;
import mod.surviving_the_aftermath.event.battle_tracker.PlayerBattleTracker;
import mod.surviving_the_aftermath.raid.IRaid;
import mod.surviving_the_aftermath.raid.RaidManager;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Mod.EventBusSubscriber
public class PlayerEvent {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        InteractionHand hand = event.getHand();
        Level level = event.getLevel();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) return;

        RaidManager instance = RaidManager.getInstance();
        Map<UUID, PlayerBattleTracker> playerBattleTrackers = instance.getPlayerBattleTrackers();
        Map<UUID, IRaid> raids = instance.getRaids();

        System.out.println("playerBattleTrackers:" + playerBattleTrackers.size());
        System.out.println("raids:" + raids.size());

        raids.forEach((uuid, raid) -> {
            HashSet<UUID> enemies = raid.getEnemies();
            List<UUID> players = raid.getPlayers();
            RaidEnemyInfo raidEnemyInfo = raid.getRaidEnemyInfo();
            int wave = raid.getWave();
            int totalEnemyCount = raid.getTotalEnemyCount();
            System.out.println("enemies:" + enemies.size());
            System.out.println("players:" + players.size());
            System.out.println("raidEnemyInfo:" + raidEnemyInfo);
            System.out.println("wave:" + wave);
            System.out.println("totalEnemyCount:" + totalEnemyCount);
        });
    }
}
