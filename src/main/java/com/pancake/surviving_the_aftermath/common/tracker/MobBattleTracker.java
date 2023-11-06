package com.pancake.surviving_the_aftermath.common.tracker;


import com.pancake.surviving_the_aftermath.api.base.BaseTracker;
import com.pancake.surviving_the_aftermath.common.config.AftermathConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;
import java.util.UUID;


public class MobBattleTracker extends BaseTracker {
    public static final String IDENTIFIER = "mob_battle_tracker";
    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    @SubscribeEvent
    public void onLivingAddHighlight(LivingEvent.LivingTickEvent event) {
        if(!AftermathConfig.enableMobBattleTrackerHighlight.get()) return;

        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide()) return;

        manager.getAftermath(uuid).ifPresent(aftermath -> {
            Set<UUID> enemies = aftermath.getEnemies();
            if (enemies.contains(entity.getUUID())) {
                entity.setGlowingTag(true);
            }
        });
    }

}
