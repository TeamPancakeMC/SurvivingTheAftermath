package com.pancake.surviving_the_aftermath.common.raid;

import com.google.common.collect.Sets;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermath;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.condition.StructureConditionModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.util.AftermathEventUtil;
import com.pancake.surviving_the_aftermath.common.util.CodecUtils;
import com.pancake.surviving_the_aftermath.common.util.RandomUtils;
import com.pancake.surviving_the_aftermath.common.util.StructureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Arrays.stream;

public class BaseRaid extends BaseAftermath implements IRaid {
    public static final String IDENTIFIER = "raid";
    public static final Codec<BaseRaid> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            AftermathState.CODEC.fieldOf("state").forGetter(BaseRaid::getState),
            BaseRaidModule.CODEC.fieldOf("module").forGetter(BaseRaid::getModule),
            CodecUtils.setOf(CodecUtils.UUID_CODEC).fieldOf("players").forGetter(BaseRaid::getPlayers),
            Codec.FLOAT.fieldOf("progressPercent").forGetter(BaseRaid::getProgressPercent),
            BlockPos.CODEC.fieldOf("startPos").forGetter(BaseRaid::getStartPos),
            Codec.INT.fieldOf("readyTime").forGetter(BaseRaid::getReadyTime),
            Codec.INT.fieldOf("rewardTime").forGetter(BaseRaid::getRewardTime),
            CodecUtils.setOf(BlockPos.CODEC).fieldOf("spawnPos").forGetter(BaseRaid::getSpawnPos),
            CodecUtils.setOf(CodecUtils.UUID_CODEC).fieldOf("enemies").forGetter(BaseRaid::getEnemies),
            Codec.INT.fieldOf("currentWave").forGetter(BaseRaid::getCurrentWave),
            Codec.INT.fieldOf("totalEnemy").forGetter(BaseRaid::getTotalEnemy)
    ).apply(instance, BaseRaid::new));

    protected Set<UUID> enemies = Sets.newLinkedHashSet();
    protected Set<BlockPos> spawnPos = Sets.newHashSet();
    protected int currentWave = -1;
    protected int totalEnemy = 0;
    public BlockPos startPos;
    private int readyTime;
    public int rewardTime;
    public BaseRaid(AftermathState state, BaseRaidModule module, Set<UUID> players, Float progressPercent, BlockPos startPos, Integer readyTime, Integer rewardTime,
                    Set<BlockPos> spawnPos, Set<UUID> enemies, Integer currentWave, Integer totalEnemy) {
        super(state, module, players, progressPercent);
        this.startPos = startPos;
        this.readyTime = readyTime;
        this.rewardTime = rewardTime;
        this.spawnPos = spawnPos;
        this.enemies = enemies;
        this.currentWave = currentWave;
        this.totalEnemy = totalEnemy;
    }

    public BaseRaid(ServerLevel level,BlockPos startPos) {
        super(level);
        this.startPos = startPos;
        init();
    }
    public BaseRaid(BaseRaidModule module,ServerLevel level,BlockPos startPos) {
        super(module,level);
        this.startPos = startPos;
        init();
    }


    public BaseRaid() {
    }



    @Override
    protected void init() {
        SetSpawnPos(this::defaultSetSpawnPos);
        this.readyTime = getModule().getReadyTime();
        this.rewardTime = getModule().getReadyTime();
    }
    @Override
    public void tick() {
        super.tick();

        if (state == AftermathState.START){
            AftermathEventUtil.ready(this,players,level);
        }

        if (state == AftermathState.ONGOING){
            checkNextWave();
            spawnWave();
            EnemyTotalRatio();
        }

        if (state == AftermathState.CELEBRATING){
            if (rewardTime <= 0){
                end();
                return;
            }
            createRewards();
            rewardTime--;
        }
    }

    private void EnemyTotalRatio(){
        if (enemies.isEmpty()) return;
        this.progressPercent = enemies.size() / (float) totalEnemy;
    }

    protected void spawnWave() {
        if (enemies.isEmpty() && state == AftermathState.ONGOING){
            getModule().getWaves().get(currentWave).forEach(this::spawnEntities);
        }
        enemies.removeIf(uuid -> level.getEntity(uuid) == null);
    }

    private void spawnEntities(IEntityInfoModule entityInfoModule) {
        if (players.isEmpty()) return;
        List<LazyOptional<Entity>> arrayList = entityInfoModule.spawnEntity(level);
        for (LazyOptional<Entity> lazyOptional : arrayList) {
            lazyOptional.ifPresent(entity -> {
                if (entity instanceof Mob mob) {
                    setMobSpawn(level,mob);
                }
            });
        }
    }

    @Override
    public void createRewards() {
        BlockPos blockPos = RandomUtils.getRandomElement(spawnPos);
        Direction dir = Direction.Plane.HORIZONTAL.stream().filter(d -> level.isEmptyBlock(blockPos.relative(d))
                && !spawnPos.contains(blockPos.relative(d))).findFirst().orElse(Direction.UP);
        Vec3 vec = Vec3.atCenterOf(blockPos);
        getModule().getRewards().getWeightedList().getRandomValue(level.random).ifPresent(reward -> {
            ItemEntity itemEntity = new ItemEntity(level, vec.x, vec.y, vec.z, new ItemStack(reward),
                    dir.getStepX() * 0.2, 0.2, dir.getStepZ() * 0.2f);
            itemEntity.setInvulnerable(true);
            level.addFreshEntity(itemEntity);
        });
    }

    public void setMobSpawn(ServerLevel level, Mob mob) {
        mob.setPersistenceRequired();
        Player target = randomPlayersUnderAttack();
        mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, target.getUUID());
        mob.setTarget(target);

        try {
            BlockPos blockPos = RandomUtils.getRandomElement(spawnPos);
            mob.moveTo(blockPos.getX() + 0.5, blockPos.getY() , blockPos.getZ()+ 0.5);
        } catch (NullPointerException e){
            mob.moveTo(this.startPos.getX() + 0.5, this.startPos.getY() , this.startPos.getZ()+ 0.5);
        }

        if (join(mob)) {
            level.addFreshEntityWithPassengers(mob);
        }
    }
    public Player randomPlayersUnderAttack(){
        return level.getPlayerByUUID(RandomUtils.getRandomElement(getPlayers()));
    }

    public boolean join(Entity entity) {
        if (state == AftermathState.ONGOING &&
                Math.sqrt(entity.blockPosition().distSqr(startPos)) < getRadius() &&
                enemies.add(entity.getUUID())) {
            totalEnemy++;
            return true;
        }
        return false;
    }


    protected void checkNextWave(){
        if (enemies.isEmpty()){
            if(this.currentWave >= getModule().getWaves().size() - 1) {
                AftermathEventUtil.victory(this,players,level);
            } else {
                currentWave++;
                totalEnemy = 0;
            }
        }
    }

    @Override
    public boolean isCreate(Level level, BlockPos pos, @Nullable Player player) {
        boolean create = super.isCreate(level, pos, player);
        boolean noneMatch = AftermathManager.getInstance().getAftermathMap().values().stream()
                .filter(aftermath -> aftermath instanceof IRaid)
                .map(aftermath -> (IRaid) aftermath)
                .noneMatch(raid -> raid.getStartPos().distSqr(startPos) < Math.pow(raid.getRadius(), 2));
        return create && noneMatch;
    }
    public void SetSpawnPos(SpawnPosHandler handler) {
        handler.handleSpawnPos(this.level,this.startPos);
    }

    private void defaultSetSpawnPos(Level level,BlockPos startPos) {
        if (level instanceof  ServerLevel serverLevel){
            List<IConditionModule> conditions = getModule().getConditions();
            if (conditions == null){
                this.startPos = startPos;
                this.spawnPos.add(startPos);
                return;
            }
            Optional<StructureConditionModule> module = conditions.stream()
                    .filter(condition -> condition instanceof StructureConditionModule)
                    .map(condition -> (StructureConditionModule) condition)
                    .findFirst();
            if (module.isPresent()){
                StructureUtils.handleDataMarker(serverLevel, startPos, module.get().getResourceLocation(), (serverLevel1, metadata, blockInfo, startPos1) -> {
                    this.startPos = startPos1;
                    BlockPos metaPos = blockInfo.pos();
                    setMobSpawnPos(serverLevel1,metadata,startPos1,metaPos);
                });
            }else {
                this.startPos = startPos;
                this.spawnPos.add(startPos);
            }
        }
    }

    public void setMobSpawnPos(ServerLevel serverLevel, String metadata, BlockPos startPos, BlockPos metaPos) {
        spawnPos.add(metaPos);
    }

    @Override
    public void updateProgress() {
        super.updateProgress();

        if (state == AftermathState.READY){
            ready();
        }
    }


    public void ready(){
        if (readyTime <= 0){
            AftermathEventUtil.ongoing(this,players,level);
            return;
        }
        this.progressPercent = 1 - (float) readyTime / getModule().getReadyTime();
        readyTime--;
    }

    @Override
    public Predicate<? super ServerPlayer> validPlayer() {
        Predicate<ServerPlayer> predicate = (Predicate<ServerPlayer>) super.validPlayer();
        return predicate.and(player -> Math.sqrt(player.distanceToSqr(Vec3.atCenterOf(startPos))) < getRadius());
    }

    @Override
    public BaseRaidModule getModule() {
        return (BaseRaidModule) super.getModule();
    }

    @Override
    public Codec<? extends IAftermath> codec() {
        return CODEC;
    }

    @Override
    public IAftermath type() {
        return ModAftermathModule.BASE_RAID.get();
    }


    @Override
    public ResourceLocation getRegistryName() {
        return SurvivingTheAftermath.asResource(IDENTIFIER);
    }

    @Override
    public ResourceLocation getBarsResource() {
        return null;
    }

    @Override
    public int[] getBarsOffset() {
        return null;
    }

    @Override
    public BlockPos getStartPos() {
        return startPos;
    }

    @Override
    public int getRadius() {
        return 50;
    }
    public int getReadyTime() {
        return readyTime;
    }

    public int getRewardTime() {
        return rewardTime;
    }

    public Set<BlockPos> getSpawnPos() {
        return spawnPos;
    }

    public void addSpawnPos(BlockPos pos) {
        spawnPos.add(pos);
    }

    public Set<UUID> getEnemies() {
        return enemies;
    }

    public int getCurrentWave() {
        return currentWave;
    }

    public int getTotalEnemy() {
        return totalEnemy;
    }

    @FunctionalInterface
    public interface SpawnPosHandler {
        void handleSpawnPos(Level level, BlockPos pos);
    }
}