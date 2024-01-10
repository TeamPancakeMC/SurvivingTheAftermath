package com.pancake.surviving_the_aftermath.common.tracker;


import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.base.BaseTracker;
import com.pancake.surviving_the_aftermath.common.config.AftermathConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Set;
import java.util.UUID;


public class MobBattleTracker extends BaseTracker {
    public static final Codec<MobBattleTracker> CODEC = Codec.unit(MobBattleTracker::new);
    public static final String IDENTIFIER = "mob_battle_tracker";


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

    @Override
    public Codec<? extends ITracker> codec() {
        return null;
    }

    @Override
    public ITracker type() {
        return null;
    }
}
