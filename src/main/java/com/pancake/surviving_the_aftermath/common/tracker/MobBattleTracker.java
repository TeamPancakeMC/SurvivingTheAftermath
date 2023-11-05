package com.pancake.surviving_the_aftermath.common.tracker;


import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.aftermath.BaseTracker;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.common.config.AftermathConfig;
import com.pancake.surviving_the_aftermath.common.raid.api.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


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

        manager.getAftermathMap().get(uuid).getEnemies().forEach(uuid -> {
            if (uuid.equals(entity.getUUID())) {
                entity.setGlowingTag(true);
            }
        });
    }
}
