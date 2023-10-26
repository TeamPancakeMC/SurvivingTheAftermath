package com.pancake.surviving_the_aftermath.raid;

import com.pancake.surviving_the_aftermath.raid.api.BaseRaid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;

public class NetherRaid extends BaseRaid {
    public static final String IDENTIFIER = "NetherRaid";
    public NetherRaid(ServerLevel level) {
        super(level);
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
        return true;
    }

    @Override
    public ResourceLocation getBarsResource() {
        return null;
    }

    @Override
    public int[] getBarsOffset() {
        return new int[0];
    }

    @Override
    public void spawnRewards() {

    }

    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }
}
