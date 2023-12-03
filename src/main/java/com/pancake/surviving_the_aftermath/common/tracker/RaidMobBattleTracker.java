package com.pancake.surviving_the_aftermath.common.tracker;


import com.pancake.surviving_the_aftermath.common.config.AftermathConfig;
import com.pancake.surviving_the_aftermath.common.raid.api.BaseRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class RaidMobBattleTracker extends MobBattleTracker{
    public static final String IDENTIFIER = "raid_mob_battle_tracker";

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @SubscribeEvent
    public void onLivingRestrictedRange(LivingEvent.LivingTickEvent event) {
        if(!AftermathConfig.enableMobBattleTrackerRestrictedRange.get()) return;

        manager.getAftermath(uuid).ifPresent(aftermath -> {
            if (aftermath instanceof BaseRaid<?> raid){
                LivingEntity entity = event.getEntity();
                Level level = entity.level();
                if (level.isClientSide) return;
                Set<UUID> enemies = raid.getEnemies();
                if (enemies.contains(entity.getUUID())) {
                    BlockPos centerPos = raid.getCenterPos();
                    BlockPos pos = entity.blockPosition();
                    double distance = Math.sqrt(centerPos.distSqr(pos));
                    if (distance > 50) {
                        double value = level.random.nextInt(30);
                        BlockPos blockPos = new BlockPos((int) (centerPos.getX() + value), centerPos.getY() +  level.random.nextInt(10,20), (int) (centerPos.getZ() + value));
                        entity.addTag("restricted_range:" + blockPos.toShortString());
                    }
                }
            }
        });
    }
}
