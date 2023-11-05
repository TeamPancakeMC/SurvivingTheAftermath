package com.pancake.surviving_the_aftermath.common.tracker;

import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.common.config.AftermathConfig;
import com.pancake.surviving_the_aftermath.common.raid.api.BaseRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class RaidMobBattleTracker extends MobBattleTracker{
    public static final String IDENTIFIER = "raid_mob_battle_tracker";

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @SubscribeEvent
    public void onLivingRestrictedRange(LivingEvent.LivingTickEvent event) {
        if(!AftermathConfig.enableMobBattleTrackerRestrictedRange.get()) return;

        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        IAftermath<BaseAftermathModule> aftermath = manager.getAftermathMap().get(uuid);
        if (aftermath instanceof BaseRaid<?> raid){
            int radius = raid.getRadius();
            raid.getEnemies().forEach(uuid -> {
                BlockPos centerPos = raid.getCenterPos();
                if (uuid.equals(entity.getUUID())) {
                    if (entity.distanceToSqr(centerPos.getCenter()) > Math.pow(radius, 2)) {
                        double value = entity.level().random.nextInt(20);
                        entity.teleportTo(centerPos.getX() + value, centerPos.getY() + 20, centerPos.getZ() + value);
                    }
                }
            });
        }
    }
}
