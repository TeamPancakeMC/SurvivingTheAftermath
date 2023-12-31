package com.pancake.surviving_the_aftermath.common.raid;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.IAftermathFactory;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
import com.pancake.surviving_the_aftermath.api.PortalShapeAccessor;
import com.pancake.surviving_the_aftermath.common.raid.api.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import com.pancake.surviving_the_aftermath.common.structure.NetherRaidStructure;
import com.pancake.surviving_the_aftermath.common.tracker.RaidMobBattleTracker;
import com.pancake.surviving_the_aftermath.common.tracker.RaidPlayerBattleTracker;
import com.pancake.surviving_the_aftermath.common.util.AftermathEventUtil;
import com.pancake.surviving_the_aftermath.common.util.RandomUtils;
import com.pancake.surviving_the_aftermath.common.util.SpawnEntityUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;

public class NetherRaid extends BaseRaid<NetherRaidModule> {
    public static final String IDENTIFIER = "nether_raid";
    private static final ResourceLocation BARS_RESOURCE = SurvivingTheAftermath.asResource("textures/gui/nether_raid_bars.png");
    protected Set<BlockPos> spawnPos = Sets.newHashSet();
    private int readyTime;
    private static final int MAX_REWARD_TIME = 10 * 20;
    private int rewardTime = MAX_REWARD_TIME;
    public NetherRaid(ServerLevel level, BlockPos centerPos ,PortalShape portalShape) {
        super(level,centerPos);
        setSpawnPos(portalShape);
        this.readyTime = getModule().getReadyTime();
    }

    public NetherRaid(ServerLevel level, CompoundTag compoundTag) {
        super(level);
        this.deserializeNBT(compoundTag);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        compoundTag.putInt(Constant.READY_TIME, readyTime);
        compoundTag.putInt(Constant.REWARD_TIME, rewardTime);

        ListTag listTag = new ListTag();
        spawnPos.forEach(blockPos -> listTag.add(NbtUtils.writeBlockPos(blockPos)));
        compoundTag.put(Constant.SPAWN_POS, listTag);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        super.deserializeNBT(nbt);

        this.readyTime = nbt.getInt(Constant.READY_TIME);
        this.rewardTime = nbt.getInt(Constant.REWARD_TIME);

        ListTag tags = nbt.getList(Constant.SPAWN_POS, Tag.TAG_COMPOUND);
        tags.forEach(tag -> {
            BlockPos blockPos = NbtUtils.readBlockPos((CompoundTag) tag);
            spawnPos.add(blockPos);

        });
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
    public boolean isCreate() {
        return level.structureManager().getAllStructuresAt(this.centerPos)
                .containsKey(level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID))
                && super.isCreate();
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
        super.spawnRewards();
        BlockPos blockPos = RandomUtils.getRandomElement(spawnPos);
        Direction dir = Direction.Plane.HORIZONTAL.stream().filter(d -> level.isEmptyBlock(blockPos.relative(d))
                && !spawnPos.contains(blockPos.relative(d))).findFirst().orElse(Direction.UP);
        Vec3 vec = Vec3.atCenterOf(blockPos);
        module.getRewardList().getRandomValue(level.random).ifPresent(reward -> {
            ItemEntity itemEntity = new ItemEntity(level, vec.x, vec.y, vec.z, new ItemStack(reward),
                    dir.getStepX() * 0.2, 0.2, dir.getStepZ() * 0.2f);
            itemEntity.setInvulnerable(true);
            level.addFreshEntity(itemEntity);
        });
    }

    @Override
    public NetherRaidModule getModule() {
        return (NetherRaidModule) module;
    }


    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }

    protected void updateStructure() {
        StructureStart start = this.level.structureManager().getStructureAt(centerPos, Objects.requireNonNull(this.level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID)));
        if (start != StructureStart.INVALID_START) {
            Optional<StructureTemplate> template = this.level.getStructureManager().get(NetherRaidStructure.STRUCTURE_TRANSFORMED);
            template.ifPresent(t -> {
                if (start.getPieces().get(0) instanceof TemplateStructurePiece piece) {
                    BlockPos pos = piece.templatePosition();
                    StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(piece.getRotation()).setMirror(Mirror.NONE)
                            .addProcessor(new BlockIgnoreProcessor(ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK, Blocks.NETHER_PORTAL, Blocks.OBSIDIAN)))
                            .addProcessor(new StructureProcessor() {

                                @Override
                                @NotNull
                                protected StructureProcessorType<?> getType() {
                                    return null;
                                }

                                @Override
                                public StructureTemplate.StructureBlockInfo process(@NotNull LevelReader levelReader,
                                                                                    @NotNull BlockPos p_74141_,
                                                                                    @NotNull BlockPos p_74142_,
                                                                                    @NotNull StructureTemplate.StructureBlockInfo blockInfo,
                                                                                    @NotNull StructureTemplate.StructureBlockInfo relativeBlockInfo,
                                                                                    @NotNull StructurePlaceSettings p_74145_,
                                                                                    @Nullable StructureTemplate template) {
                                    if (level.random.nextFloat() < 0.9) {
                                        return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(),
                                                levelReader.getBlockState(relativeBlockInfo.pos()), relativeBlockInfo.nbt());
                                    } else {
                                        return relativeBlockInfo;
                                    }
                                }
                            });
                    t.placeInWorld(this.level, pos, pos, settings, this.level.random, 2);
                }
            });
        }
    }

    @Override
    public Predicate<? super ServerPlayer> validPlayer() {
        Predicate<ServerPlayer> predicate = (Predicate<ServerPlayer>) super.validPlayer();
        return predicate.and((player) -> Math.sqrt(player.distanceToSqr(Vec3.atCenterOf(centerPos))) < getRadius());
    }

    @Override
    public void updateProgress() {
        super.updateProgress();

        if (state == AftermathState.START){
            ready();
            return;
        }
        if (state == AftermathState.READY){
            ready();
            return;
        }
        if (state == AftermathState.ONGOING){
            AftermathEventUtil.ongoing(this, players, level);
            checkNextWave();
            spawnWave();
            EnemyTotalRatio();
        }
    }

    @Override
    public void ready(){
        super.ready();
        if (readyTime <= 0){
            AftermathEventUtil.ongoing(this, players, level);
            return;
        }
        this.progressPercent = 1 - (float) readyTime / getModule().getReadyTime();
        readyTime--;
    }

    @Override
    protected List<LazyOptional<Entity>> spawnEntities(IEntityInfoModule module) {
        if (players.isEmpty()) return Collections.emptyList();
        List<LazyOptional<Entity>> arrayList = module.spawnEntity(level);
        for (LazyOptional<Entity> lazyOptional : arrayList) {
            lazyOptional.ifPresent(entity -> {
                if (entity instanceof Mob mob){
                    BlockPos blockPos = RandomUtils.getRandomElement(spawnPos);
                    mob.moveTo(blockPos.getX() + 0.5, blockPos.getY() , blockPos.getZ()+ 0.5);
                    mob.setPersistenceRequired();
                    Player target = randomPlayersUnderAttack();
                    mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, target.getUUID());
                    mob.setTarget(target);

                    for (var slot : EquipmentSlot.values()) {
                        mob.setDropChance(slot, 0);
                    }


                    if (entity instanceof AbstractPiglin piglin) {
                        piglin.setImmuneToZombification(true);
                    }
                    if (entity instanceof Hoglin hoglin) {
                        hoglin.setImmuneToZombification(true);
                    }
                    if (entity instanceof Slime slime) {
                        slime.finalizeSpawn(level, level.getCurrentDifficultyAt(slime.blockPosition()), MobSpawnType.EVENT, null, null);
                    }
                    if (entity instanceof Ghast ghast) {
                        ghast.setPos(ghast.getX(), ghast.getY() + 20, ghast.getZ());
                    }

                    for (int i = 0; i < 5; i++) {
                        if (isBlocked(level, mob)) {
                            Direction.Plane.HORIZONTAL.stream().forEach(direction -> {
                                if (level.isEmptyBlock(mob.blockPosition().relative(direction))) {
                                    mob.moveTo(Vec3.atCenterOf(mob.blockPosition().relative(direction)));
                                }
                            });
                        }
                    }


                    enemies.add(mob.getUUID());
                    totalEnemy++;
                    level.addFreshEntityWithPassengers(mob);
                }
            });
        }
        return arrayList;
    }

    private boolean isBlocked(ServerLevel level, Entity entity) {
        for (var shape : level.getBlockCollisions(entity, entity.getBoundingBox())) {
            return !shape.isEmpty();
        }
        return false;
    }



    protected void spawnWave() {
        if (enemies.isEmpty() && state == AftermathState.ONGOING){
            LOGGER.info(getUniqueIdentifier() +": spawn wave " + currentWave);
            module.getWaves().get(currentWave).forEach(this::spawnEntities);
            updateStructure();
        }
        enemies.removeIf(uuid -> level.getEntity(uuid) == null);
    }

    private void EnemyTotalRatio(){
        if (enemies.isEmpty()) return;
        this.progressPercent = enemies.size() / (float) totalEnemy;
    }

    protected void checkNextWave(){
        if (enemies.isEmpty()){
            if(this.currentWave >= module.getWaves().size() - 1) {
                AftermathEventUtil.victory(this, players, level);
            } else {
                currentWave++;
                totalEnemy = 0;
                AftermathEventUtil.ongoing(this, players, level);
            }
        }
    }

    protected void setSpawnPos(PortalShape portalShape){
        spawnPos.clear();
        spawnPos.add(centerPos);
        PortalShapeAccessor portalShapeMixin = (PortalShapeAccessor ) portalShape;
        BlockPos bottomLeft = portalShapeMixin.survivingTheAftermath$getBottomLeft();
        int height = portalShapeMixin.survivingTheAftermath$getHeight();
        int width = portalShapeMixin.survivingTheAftermath$getWidth();
        Direction rightDir = portalShapeMixin.survivingTheAftermath$getRightDir();
        BlockPos.betweenClosed(bottomLeft, bottomLeft.relative(Direction.UP, height - 1).relative(rightDir, width - 1))
                    .forEach(blockPos -> spawnPos.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
    }

    @Override
    public void bindTrackers() {
        super.bindTrackers();
        API.getTracker(uuid, RaidMobBattleTracker.IDENTIFIER,
                        RaidPlayerBattleTracker.IDENTIFIER)
                .forEach(this::addTracker);
    }

    @Override
    public boolean join(Entity entity) {
        return entity.getType() == EntityType.MAGMA_CUBE && super.join(entity);
    }

    public static class Factory implements IAftermathFactory {
        @Override
        public IAftermath<BaseAftermathModule> create(ServerLevel level, CompoundTag compound) {
            return new NetherRaid(level, compound);
        }
    }


}
