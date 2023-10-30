package com.pancake.surviving_the_aftermath.common.raid;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
import com.pancake.surviving_the_aftermath.common.raid.api.BaseRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class NetherRaid extends BaseRaid {
    public static final String IDENTIFIER = "NetherRaid";
    private static final ResourceLocation BARS_RESOURCE = SurvivingTheAftermath.asResource("textures/gui/nether_raid_bars.png");
    public NetherRaid(ServerLevel level, BlockPos centerPos) {
        super(level,centerPos);
    }

    public NetherRaid(ServerLevel level, CompoundTag compoundTag) {
        super(level, compoundTag);
    }

    @Override
    public void tick() {
        super.tick();
        System.out.println(module);
    }

    @Override
    public boolean isCreate() {
//        return super.isCreate();
        return level.structureManager().getAllStructuresAt(this.centerPos)
                .containsKey(level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID));
    }

    @Override
    public ResourceLocation getBarsResource() {
        return BARS_RESOURCE;
    }

    @Override
    public int[] getBarsOffset() {
        return new int[]{192,23,182,4,5,4};
    }

    @Override
    public void spawnRewards() {

    }

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }
}
