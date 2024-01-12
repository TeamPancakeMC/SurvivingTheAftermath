package com.pancake.surviving_the_aftermath.common.raid;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;

public class NetherRaid extends BaseRaid<NetherRaidModule> {
    public static final String IDENTIFIER = "nether_raid";

    @Override
    public Codec<? extends IAftermath<BaseAftermathModule>> codec() {
        return null;
    }

    @Override
    public IAftermath<BaseAftermathModule> type() {
        return null;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return SurvivingTheAftermath.asResource(IDENTIFIER);
    }

    @Override
    public void insertTag(LivingEntity entity) {
        entity.getPersistentData().put(IDENTIFIER, StringTag.valueOf("enemies"));
    }
}