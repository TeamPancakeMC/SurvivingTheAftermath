package com.pancake.surviving_the_aftermath.common.raid;

import com.google.common.collect.ImmutableList;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.PortalShapeAccessor;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
import com.pancake.surviving_the_aftermath.common.raid.module.BaseRaidModule;
import com.pancake.surviving_the_aftermath.common.structure.NetherRaidStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.level.portal.PortalShape;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.*;

public class NetherRaid extends BaseRaid {
    public static final String IDENTIFIER = "nether_raid";
    private PortalShape portalShape;
    private Direction rightDir;

    public NetherRaid(ServerLevel level, BlockPos startPos) {
        super(level, startPos);
    }

    public NetherRaid() {
    }

    @Override
    protected void init() {
        setDir(level,startPos);
        super.init();
    }

    @Override
    public void setMobSpawnPos(ServerLevel serverLevel, String metadata, BlockPos startPos, BlockPos pos) {
        PortalShapeAccessor portalShapeMixin = (PortalShapeAccessor) portalShape;
        BlockPos bottomLeft = portalShapeMixin.survivingTheAftermath$getBottomLeft();
        int height = portalShapeMixin.survivingTheAftermath$getHeight();
        int width = portalShapeMixin.survivingTheAftermath$getWidth();
        Direction rightDir = portalShapeMixin.survivingTheAftermath$getRightDir();
        BlockPos.betweenClosed(bottomLeft, bottomLeft.relative(Direction.UP, height - 1).relative(rightDir, width - 1))
                .forEach(blockPos -> spawnPos.add(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ())));
    }

    private void setDir(ServerLevel serverLevel,BlockPos pos){
        PortalShape.findEmptyPortalShape(serverLevel, pos, Direction.Axis.X).ifPresent(portalShape -> {
            PortalShapeAccessor portalShapeMixin = (PortalShapeAccessor) portalShape;
            this.rightDir = portalShapeMixin.survivingTheAftermath$getRightDir();
            this.portalShape = portalShape;
        });
    }

    @Override
    public void setMobSpawn(ServerLevel level, Mob mob) {
        super.setMobSpawn(level, mob);


        if (mob instanceof AbstractPiglin piglin) {
            piglin.setImmuneToZombification(true);
        }
        if (mob instanceof Hoglin hoglin) {
            hoglin.setImmuneToZombification(true);
        }
        if (mob instanceof Slime slime) {
            slime.finalizeSpawn(level, level.getCurrentDifficultyAt(slime.blockPosition()), MobSpawnType.EVENT, null, null);
        }
        if (mob instanceof Ghast ghast) {
            ghast.setPos(ghast.getX(), ghast.getY() + 20, ghast.getZ());
        }

    }

    @Override
    protected void spawnWave() {
        super.spawnWave();
        if (enemies.isEmpty() && state == AftermathState.ONGOING){
            updateStructure();
        }
    }



    protected void updateStructure() {
        StructureStart start = this.level.structureManager().getStructureAt(startPos, Objects.requireNonNull(this.level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID)));
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
}
