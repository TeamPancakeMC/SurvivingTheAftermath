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
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoWithEquipmentModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.util.CodecUtils;
import com.pancake.surviving_the_aftermath.util.RandomUtils;
import com.pancake.surviving_the_aftermath.util.StructureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.function.Predicate;

import static java.util.Arrays.stream;

public class BaseRaid extends BaseAftermath implements IRaid {
    public static final String IDENTIFIER = "raid";
    public static final Codec<BaseRaid> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            CodecUtils.UUID_CODEC.fieldOf("uuid").forGetter(BaseRaid::getUUID),
            AftermathState.CODEC.fieldOf("state").forGetter(BaseRaid::getState),
            BaseRaidModule.CODEC.fieldOf("module").forGetter(BaseRaid::getModule),
            CodecUtils.setOf(CodecUtils.UUID_CODEC).fieldOf("players").forGetter(BaseRaid::getPlayers),
            Codec.FLOAT.fieldOf("progressPercent").forGetter(BaseRaid::getProgressPercent),
            BlockPos.CODEC.fieldOf("startPos").forGetter(BaseRaid::getStartPos),
            Codec.INT.fieldOf("readyTime").forGetter(BaseRaid::getReadyTime),
            Codec.INT.fieldOf("rewardTime").forGetter(BaseRaid::getRewardTime)
    ).apply(instance, BaseRaid::new));

    public BaseRaid(UUID uuid,AftermathState state,BaseRaidModule module, Set<UUID> players, float progressPercent,BlockPos startPos,int readyTime,int rewardTime) {
        super(state,module, players, progressPercent);
        this.startPos = startPos;
        this.readyTime = readyTime;
        this.rewardTime = rewardTime;
    }

    public BaseRaid(ServerLevel level,BlockPos startPos) {
        super(level);
        this.startPos = startPos;
        SetSpawnPos(level,startPos);
        this.readyTime = getModule().getReadyTime();
        this.rewardTime = getModule().getReadyTime();
    }
//    public BaseRaid(BaseRaidModule module,ServerLevel level,BlockPos startPos) {
//        super(module,level);
//        this.startPos = startPos;
//        SetSpawnPos(level,startPos);
//        this.readyTime = getModule().getReadyTime();
//        this.rewardTime = getModule().getReadyTime();
//    }


    public BaseRaid() {
    }
    protected Set<UUID> enemies = Sets.newLinkedHashSet();
    protected Set<BlockPos> spawnPos = Sets.newHashSet();
    protected int currentWave = -1;
    protected int totalEnemy = 0;
    public BlockPos startPos;
    private int readyTime;
    public int rewardTime;

    @Override
    public void tick() {
        super.tick();

        if (state == AftermathState.START){
            state = AftermathState.READY;
        }

        if (state == AftermathState.ONGOING){
            checkNextWave();
            spawnWave();
            EnemyTotalRatio();
        }

        if (state == AftermathState.CELEBRATING){
            System.out.println("庆祝状态 :" + rewardTime);
            if (rewardTime <= 0){
                end();
                return;
            }
            createRewards();
            rewardTime--;
        }
        System.out.println("state: " + state);
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
                    if (entityInfoModule instanceof EntityInfoWithEquipmentModule entityInfoWithEquipmentModule){
                        mob.setCanPickUpLoot(entityInfoWithEquipmentModule.isCanDrop());
                    }
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
        System.out.println("createRewards");
    }

    private void setMobSpawn(ServerLevel level, Mob mob) {
        mob.setPersistenceRequired();
        mob.setTarget(randomPlayersUnderAttack());

        System.out.println("spawnPos size :" + spawnPos.size());

        BlockPos blockPos = RandomUtils.getRandomElement(spawnPos);
        mob.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());

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
                state = AftermathState.VICTORY;
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
    public void SetSpawnPos(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel) {
            List<IConditionModule> conditions = getModule().getConditions();
            if (conditions == null){
                startPos = pos;
                spawnPos.add(pos);
                return;
            }
            Optional<StructureConditionModule> module = conditions.stream()
                    .filter(condition -> condition instanceof StructureConditionModule)
                    .map(condition -> (StructureConditionModule) condition)
                    .findFirst();
            if (module.isPresent()){
                StructureUtils.handleDataMarker(serverLevel, pos, SurvivingTheAftermath.asResource("nether_invasion_portal"), (serverLevel1, metadata, blockInfo, startPos) -> {
                    this.startPos = startPos;
                    BlockPos metaPos = blockInfo.pos();
                    setMobSpawnPos(serverLevel1,metadata,startPos,metaPos);

                    System.out.println("handleDataMarker");
                });
            }else {
                startPos = pos;
                spawnPos.add(pos);

                System.out.println("SetSpawnPos");
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
            state = AftermathState.ONGOING;
            return;
        }
        this.progressPercent = 1 - (float) readyTime / getModule().getReadyTime();
        readyTime--;
    }

    @Override
    public Predicate<? super ServerPlayer> validPlayer() {
        Predicate<ServerPlayer> predicate = (Predicate<ServerPlayer>) super.validPlayer();
        return predicate.and((player) -> Math.sqrt(player.distanceToSqr(Vec3.atCenterOf(startPos))) < getRadius());
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
    public BlockPos getStartPos() {
        return startPos;
    }

    @Override
    public int getRadius() {
        return 50;
    }
    private int getReadyTime() {
        return readyTime;
    }

    public int getRewardTime() {
        return rewardTime;
    }
}