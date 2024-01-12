package com.pancake.surviving_the_aftermath.common.raid;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.util.RandomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import java.util.*;

public class BaseRaid<T extends BaseRaidModule> extends BaseAftermath<BaseRaidModule> implements IRaid {
    public static final String IDENTIFIER = "raid";
    protected int currentWave = -1;
    protected int totalEnemy = 0;
    protected BlockPos centerPos;

    public BaseRaid(AftermathState state, Set<UUID> players, Set<UUID> enemies, BaseRaidModule module, float progressPercent, int currentWave, int totalEnemy, BlockPos centerPos) {
        super(state, players, enemies, module, progressPercent);
        this.currentWave = currentWave;
        this.totalEnemy = totalEnemy;
        this.centerPos = centerPos;
    }
    public BaseRaid(Level level, BlockPos pos, Player player){
        super(level,pos,player);
    }

    public BaseRaid() {

    }

    @Override
    public void tick() {
        super.tick();
//        if (state == AftermathState.CELEBRATING){
//            if (rewardTime <= 0){
//                end();
//                return;
//            }
//            spawnRewards();
//            rewardTime--;
//        }
    }

    @Override
    public boolean isCreate(Level level, BlockPos pos, Player player) {
        boolean create = super.isCreate(level, pos, player);
        Map<UUID, IAftermath<IAftermathModule>> aftermathMap = MANAGER.getAftermathMap();
        boolean noneMatch = aftermathMap.values().stream()
                .filter(aftermath -> aftermath instanceof IRaid)
                .map(aftermath -> (IRaid) aftermath)
                .noneMatch(raid -> raid.getCenterPos().distSqr(centerPos) < Math.pow(raid.getRadius(), 2));
        return create && noneMatch;
    }

    @Override
    public ResourceLocation getRegistryName() {
        return SurvivingTheAftermath.asResource(IDENTIFIER);
    }

    @Override
    public void insertTag(LivingEntity entity) {
        entity.getPersistentData().put(IDENTIFIER, StringTag.valueOf("enemies"));
    }

    public Player randomPlayersUnderAttack(){
        return level.getPlayerByUUID(RandomUtils.getRandomElement(getPlayers()));
    }
    public boolean join(Entity entity) {
        if (state == AftermathState.ONGOING &&
                Math.sqrt(entity.blockPosition().distSqr(centerPos)) < getRadius() &&
                enemies.add(entity.getUUID())) {
            totalEnemy++;
            return true;
        }
        return false;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getTotalEnemy() {
        return totalEnemy;
    }

    @Override
    public BlockPos getCenterPos() {
        return centerPos;
    }

    @Override
    public int getRadius() {
        return 50;
    }

    @Override
    public Codec<? extends IAftermath<BaseAftermathModule>> codec() {
        return null;
    }

    @Override
    public IAftermath<BaseAftermathModule> type() {
        return null;
    }
}