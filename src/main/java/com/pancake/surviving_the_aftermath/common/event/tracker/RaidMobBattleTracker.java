package com.pancake.surviving_the_aftermath.common.event.tracker;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.base.BaseTracker;
import com.pancake.surviving_the_aftermath.common.config.AftermathConfig;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.raid.BaseRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;


import java.util.UUID;

public class RaidMobBattleTracker extends BaseTracker {

    @SubscribeEvent
    public void onLivingRestrictedRange(LivingEvent.LivingTickEvent event) {
        if (!AftermathConfig.enableMobBattleTrackerRestrictedRange.get()) return;

        LivingEntity entity = event.getEntity();
        CompoundTag persistentData = entity.getPersistentData();
        AftermathManager instance = AftermathManager.getInstance();
        if (persistentData.contains(BaseRaid.IDENTIFIER)) {
            UUID uuid = persistentData.getUUID("raid_uuid");
            instance.getAftermath(uuid).ifPresent(raid -> {
                if (raid instanceof BaseRaid baseRaid){
                    int radius = baseRaid.getRadius();
                    BlockPos startPos = baseRaid.getStartPos();
                    if (Math.sqrt(entity.distanceToSqr(Vec3.atCenterOf(startPos))) < radius) {
                        double value = entity.getRandom().nextInt(30);
                        BlockPos blockPos = new BlockPos((int) (startPos.getX() + value), startPos.getY() +  entity.getRandom().nextInt(10,20), (int) (startPos.getZ() + value));
                        entity.getPersistentData().put("restricted_range", NbtUtils.writeBlockPos(blockPos));
                    } else {
                        entity.getPersistentData().remove("restricted_range");
                    }
                }
            });

        }
    }

    @Override
    public Codec<? extends ITracker> codec() {
        return Codec.unit(new RaidMobBattleTracker());
    }

    @Override
    public ITracker type() {
        return ModAftermathModule.RAID_MOB_BATTLE_TRACKER.get();
    }
}
