package mod.surviving_the_aftermath.event.battle_tracker;

import mod.surviving_the_aftermath.raid.IRaid;
import mod.surviving_the_aftermath.raid.RaidManager;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.UUID;

public class MobBattleTracker {
    private final UUID currentRaidId;


    public MobBattleTracker(UUID currentRaidId) {
        this.currentRaidId = currentRaidId;
    }

    @SubscribeEvent
    public void onLivingRestrictedRange(LivingEvent.LivingTickEvent event) {
        RaidManager.getInstance().getRaid(currentRaidId).ifPresent(raid -> {
            LivingEntity entity = event.getEntity();
            Level level = entity.level();
            if (level.isClientSide) return;
            HashSet<UUID> enemies = raid.getEnemies();
            if (enemies.contains(entity.getUUID()) && entity instanceof Ghast) {
                BlockPos centerPos = raid.getCenterPos();
                BlockPos pos = entity.blockPosition();
                double distance = Math.sqrt(centerPos.distSqr(pos));
                if (distance > 35) {
                    double value = level.random.nextInt(20);
                    entity.teleportTo(centerPos.getX() + value, centerPos.getY() + 20, centerPos.getZ() + value);
                }
            }
        });

    }

    //添加高亮
    @SubscribeEvent
    public void onLivingAddHighlight(LivingEvent.LivingTickEvent event) {
        RaidManager.getInstance().getRaid(currentRaidId).ifPresent(raid -> {
            LivingEntity entity = event.getEntity();
            Level level = entity.level();
            if (level.isClientSide) return;
            HashSet<UUID> enemies = raid.getEnemies();
            if (enemies.contains(entity.getUUID())) {
                entity.setGlowingTag(true);
            }
        });
    }
}
