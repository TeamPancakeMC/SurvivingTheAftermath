package com.pancake.surviving_the_aftermath.common.raid;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
import com.pancake.surviving_the_aftermath.common.raid.api.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import com.pancake.surviving_the_aftermath.common.structure.NetherRaidStructure;
import com.pancake.surviving_the_aftermath.common.util.AftermathEventUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class NetherRaid extends BaseRaid<NetherRaidModule> {
    public static final String IDENTIFIER = "NetherRaid";
    private static final ResourceLocation BARS_RESOURCE = SurvivingTheAftermath.asResource("textures/gui/nether_raid_bars.png");
    protected List<BlockPos> spawnPos = Lists.newArrayList();
    private int readyTime;
    private static final int MAX_REWARD_TIME = 10 * 20;
    private int rewardTime = MAX_REWARD_TIME;
    public NetherRaid(ServerLevel level, BlockPos centerPos ,PortalShape portalShape) {
        super(level,centerPos);
        setSpawnPos(portalShape);

        this.readyTime = getModule().getReadyTime();
        AftermathEventUtil.start(this, players, level);
    }

    public NetherRaid(ServerLevel level, CompoundTag compoundTag) {
        super(level, compoundTag);
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
        System.out.println(rewardTime);
        System.out.println(state);
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
        BlockPos blockPos = spawnPos.get(level.random.nextInt(spawnPos.size()));
        Direction dir = Direction.Plane.HORIZONTAL.getRandomDirection(level.random);
        Vec3 vec = Vec3.atCenterOf(blockPos);
        module.getRewardList().getRandomValue(level.random).ifPresent(reward ->
                level.addFreshEntity(new ItemEntity(level, vec.x, vec.y, vec.z, new ItemStack(reward),
                        dir.getStepX() * 0.2, 0.2, dir.getStepZ() * 0.2f)));
        AftermathEventUtil.celebrating(this, players, level);

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
                                protected StructureProcessorType<?> getType() {
                                    return null;
                                }

                                @Override
                                public StructureTemplate.StructureBlockInfo process(LevelReader levelReader,
                                                                                    BlockPos p_74141_, BlockPos p_74142_,
                                                                                    StructureTemplate.StructureBlockInfo blockInfo,
                                                                                    StructureTemplate.StructureBlockInfo relativeBlockInfo,
                                                                                    StructurePlaceSettings p_74145_,
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
            checkNextWave();
            spawnWave();
            progress.setProgress(EnemyTotalHealthRatio());
        }

    }

    private void ready(){
        if (readyTime <= 0){
            AftermathEventUtil.ongoing(this, players, level);
            return;
        }
        progress.setProgress(1 - (float) readyTime / getModule().getReadyTime());
        AftermathEventUtil.ready(this, players, level);
        spawnMovingParticles(centerPos, centerPos.above(), ParticleTypes.PORTAL, 1000, 1);
        readyTime--;
    }


    public void spawnMovingParticles(BlockPos startPos, BlockPos endPos, SimpleParticleType particle, int steps, int speed) {
//        ClientLevel clientLevel = Minecraft.getInstance().level;
//        if (clientLevel != null) {
//        }
    }




    @Override
    protected List<LazyOptional<Entity>>  spawnEntities(IEntityInfoModule module) {
        List<LazyOptional<Entity>> arrayList = module.spawnEntity(level);
        for (LazyOptional<Entity> lazyOptional : arrayList) {
            lazyOptional.ifPresent(entity -> {
                if (entity instanceof Mob mob){
                    BlockPos blockPos = spawnPos.get(level.random.nextInt(spawnPos.size()));
                    System.out.println(blockPos);
                    mob.moveTo(blockPos.getX() + 0.5, blockPos.getY(), blockPos.getZ() + 0.5);
                    mob.setPersistenceRequired();
                    mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, randomPlayersUnderAttack().getUUID());
                    level.addFreshEntity(entity);
                    enemies.add(mob.getUUID());
                }
            });
        }
        return arrayList;
    }

    //waves
    protected void spawnWave() {
        if (enemies.isEmpty()){
            System.out.println("生成第" + currentWave + "波敌人");
            module.getWaves().get(currentWave).forEach(this::spawnEntities);
        }
        enemies.removeIf(uuid -> level.getEntity(uuid) == null);
    }

    //获取敌人总血量比值
    private float EnemyTotalHealthRatio(){
        if (enemies.isEmpty()) return 0;
        float healthCount = 0;
        float maxHealthCount = 0;
        for (UUID uuid : enemies) {
            if (level.getEntity(uuid) instanceof LivingEntity  livingEntity){
                healthCount += livingEntity.getHealth();
                maxHealthCount += livingEntity.getMaxHealth();
            }
        }
        return healthCount / maxHealthCount;
    }

    protected void checkNextWave(){
        if (enemies.isEmpty()){
            if(this.currentWave >= module.getWaves().size() - 1) {
                AftermathEventUtil.victory(this, players, level);
            } else {
                currentWave++;
                AftermathEventUtil.ongoing(this, players, level);
            }
        }
    }

    //设置生成点
    protected void setSpawnPos(PortalShape portalShape){
        spawnPos.clear();
        try {
            BlockPos bottomLeft = SurvivingTheAftermath.getPrivateField(portalShape, "bottomLeft", BlockPos.class);
            int height = SurvivingTheAftermath.getPrivateField(portalShape, "height", Integer.class);
            int width = SurvivingTheAftermath.getPrivateField(portalShape, "width", Integer.class);
            Direction rightDir = SurvivingTheAftermath.getPrivateField(portalShape, "rightDir", Direction.class);
            BlockPos.betweenClosed(bottomLeft, bottomLeft.relative(Direction.UP, height - 1).relative(rightDir, width - 1)).forEach(blockPos -> {
                spawnPos.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()));
            });

        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.error("NetherRaid setSpawnPos error: " + e);
        }
        System.out.println(spawnPos);
    }
}
