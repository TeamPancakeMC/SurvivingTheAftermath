package com.pancake.surviving_the_aftermath.common.raid;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
import com.pancake.surviving_the_aftermath.common.raid.api.BaseRaid;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import com.pancake.surviving_the_aftermath.common.structure.NetherRaidStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Predicate;

public class NetherRaid extends BaseRaid {
    public static final String IDENTIFIER = "NetherRaid";
    private static final ResourceLocation BARS_RESOURCE = SurvivingTheAftermath.asResource("textures/gui/nether_raid_bars.png");
    protected NetherRaidModule module = this.module;
    protected List<BlockPos> spawnPos = Lists.newArrayList();
    private int readyTime;
    public NetherRaid(ServerLevel level, BlockPos centerPos ,PortalShape portalShape) {
        super(level,centerPos);
        setSpawnPos(portalShape);
        this.module = (NetherRaidModule) AFTERMATH_API.getRandomAftermathModule(getUniqueIdentifier()).orElseGet(() -> AFTERMATH_API.getAftermathMap().get(getUniqueIdentifier()).get(0));
        this.readyTime = module.getReadyTime();
        state = AftermathState.START;
    }

    public NetherRaid(ServerLevel level, CompoundTag compoundTag) {
        super(level, compoundTag);
    }

    @Override
    public void tick() {
//        if (module instanceof NetherRaidModule netherRaidModule){
//            netherRaidModule.setLeadTime(netherRaidModule.getLeadTime() - 1);
//        }
        NetherRaidModule netherRaidModule = (NetherRaidModule) module;
        super.tick();
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

        checkNextWave();

        if (state == AftermathState.ONGOING){
            spawnWave();
            progress.setProgress(EnemyTotalHealthRatio());
        }


        System.out.println(enemies.size());
        System.out.println(state);
    }

    private void ready(){
        progress.setProgress(1 - (float) readyTime / module.getReadyTime());
        if (readyTime <= 0){
            state = AftermathState.ONGOING;
        }
        AftermathEvent.Ready event = new AftermathEvent.Ready(players, level);
        MinecraftForge.EVENT_BUS.post(event);
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
                    mob.setPos(blockPos.getX(), blockPos.getY(), blockPos.getZ());
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
        float healthCount = 0;
        float maxHealthCount = 0;
        for (UUID enemy : enemies) {
            if (level.getEntity(uuid) instanceof LivingEntity  livingEntity){
                healthCount += livingEntity.getHealth();
                maxHealthCount += livingEntity.getMaxHealth();
            }
        }
        return healthCount / maxHealthCount;
    }

    protected void checkNextWave(){
        if (enemies.isEmpty()){
            currentWave++;
            if(this.currentWave >= module.getWaves().size()) {
                this.state = AftermathState.VICTORY;
            } else {
                this.state = AftermathState.ONGOING;
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
//            Direction rightDir = SurvivingTheAftermath.getPrivateField(portalShape, "rightDir", Direction.class);
            Iterable<BlockPos> blockPos = BlockPos.betweenClosed(bottomLeft, bottomLeft.relative(Direction.UP, height - 1).relative(Direction.WEST, width - 1));
            for (BlockPos blockPo : blockPos) {
                System.out.println(blockPo);
                spawnPos.add(new BlockPos(blockPo.getX(), blockPo.getY(), blockPo.getZ()));
            }
        } catch (IllegalAccessException | NoSuchFieldException e) {
            LOGGER.error("NetherRaid setSpawnPos error: " + e);
        }
        System.out.println(spawnPos);
    }
}
