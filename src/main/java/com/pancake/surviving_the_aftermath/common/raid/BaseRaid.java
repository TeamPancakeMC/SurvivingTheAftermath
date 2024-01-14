package com.pancake.surviving_the_aftermath.common.raid;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.util.CodecUtils;
import com.pancake.surviving_the_aftermath.util.RandomUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.StringTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.*;

public class BaseRaid extends BaseAftermath<BaseRaidModule> implements IRaid {
    public static final String IDENTIFIER = "raid";
    public static final Codec<BaseRaid> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtils.UUID_CODEC.fieldOf("uuid").forGetter(BaseRaid::getUUID),
            CodecUtils.setOf(CodecUtils.UUID_CODEC).fieldOf("players").forGetter(BaseRaid::getPlayers),
            CodecUtils.setOf(CodecUtils.UUID_CODEC).fieldOf("enemies").forGetter(BaseRaid::getEnemies),
            CodecUtils.setOf(BlockPos.CODEC).fieldOf("spawn_pos").forGetter(BaseRaid::getSpawnPos),
            BaseRaidModule.CODEC.fieldOf("module").forGetter(BaseRaid::getModule),
            BlockPos.CODEC.fieldOf("center_pos").forGetter(BaseRaid::getCenterPos),
            Codec.FLOAT.fieldOf("progress_percent").forGetter(BaseRaid::getProgressPercent),
            Codec.INT.fieldOf("reward_time").forGetter(BaseRaid::getRewardTime),
            Codec.INT.fieldOf("current_wave").forGetter(BaseRaid::getCurrentWave),
            Codec.INT.fieldOf("total_enemy").forGetter(BaseRaid::getTotalEnemy)
    ).apply(instance, BaseRaid::new));

    protected int currentWave = -1;
    protected int totalEnemy = 0;


    public BaseRaid(UUID uuid,Set<UUID> players, Set<UUID> enemies, Set<BlockPos> spawnPos,BaseRaidModule module, BlockPos centerPos, float progressPercent, int rewardTime, int currentWave, int totalEnemy) {
        super(players, enemies,spawnPos, module, centerPos, progressPercent, rewardTime);
        this.currentWave = currentWave;
        this.totalEnemy = totalEnemy;
    }

    public BaseRaid(Level level, BlockPos pos){
        super(level,pos);
    }

    public BaseRaid() {

    }



    @Override
    public void tick() {
        super.tick();
        if (state == AftermathState.CELEBRATING){
            if (rewardTime <= 0){
                end();
                return;
            }
            spawnRewards();
            rewardTime--;
        }
    }

    @Override
    public void ready() {

    }

    @Override
    public boolean isCreate(Level level, BlockPos pos, @Nullable Player player) {
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

    protected void checkNextWave(){
        if (enemies.isEmpty()){
            if(this.currentWave >= module.getWaves().size() - 1) {
            } else {
                currentWave++;
                totalEnemy = 0;
            }
        }
    }

    protected void spawnWave() {
        if (level instanceof ServerLevel serverLevel){
            if (enemies.isEmpty() && state == AftermathState.ONGOING){
                module.getWaves().get(currentWave).forEach(this::spawnEntities);
            }
            enemies.removeIf(uuid -> serverLevel.getEntity(uuid) == null);
        }

    }


    protected List<LazyOptional<Entity>> spawnEntities(IEntityInfoModule module) {
        if (players.isEmpty()) return Collections.emptyList();
        List<LazyOptional<Entity>> arrayList = Collections.emptyList();
        if (level instanceof ServerLevel serverLevel) {
            arrayList = module.spawnEntity(serverLevel);
            for (LazyOptional<Entity> lazyOptional : arrayList) {
                lazyOptional.ifPresent(entity -> {
                    if (entity instanceof Mob mob) {
                        setMobSpawn(serverLevel,mob);

                    }
                });
            }
        }
        return arrayList;
    }

    private void setMobSpawn(ServerLevel serverLevel, Mob mob) {
        mob.setPersistenceRequired();
        mob.setTarget(randomPlayersUnderAttack());

        BlockPos blockPos = spawnPos.stream().findAny().orElse(centerPos);
        mob.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());

        join(mob);
        serverLevel.addFreshEntityWithPassengers(mob);
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
    public BaseRaidModule getModule() {
        return (BaseRaidModule) super.getModule();
    }

    @Override
    public Codec<? extends IAftermath<IAftermathModule>> codec() {
        return CODEC;
    }

    @Override
    public IAftermath<IAftermathModule> type() {
        return ModAftermathModule.BASE_RAID.get();
    }
}