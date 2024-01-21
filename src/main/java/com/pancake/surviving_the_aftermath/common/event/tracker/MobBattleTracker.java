package com.pancake.surviving_the_aftermath.common.event.tracker;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.base.BaseTracker;
import com.pancake.surviving_the_aftermath.common.config.AftermathConfig;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.raid.BaseRaid;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Set;
import java.util.UUID;

public class MobBattleTracker extends BaseTracker {
    @SubscribeEvent
    public void onLivingAddHighlight(LivingEvent.LivingTickEvent event) {
        if(!AftermathConfig.enableMobBattleTrackerHighlight.get()) return;

        LivingEntity entity = event.getEntity();

        if (entity.getPersistentData().contains(BaseRaid.IDENTIFIER)) {
            entity.setGlowingTag(true);
        }
    }

    @Override
    public Codec<? extends ITracker> codec() {
        return Codec.unit(new MobBattleTracker());
    }

    @Override
    public ITracker type() {
        return ModAftermathModule.MOB_BATTLE_TRACKER.get();
    }
}