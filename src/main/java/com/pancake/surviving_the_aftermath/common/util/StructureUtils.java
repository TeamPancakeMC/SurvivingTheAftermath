package com.pancake.surviving_the_aftermath.common.util;

import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.List;
import java.util.Objects;

public class StructureUtils {
    public static List<BlockPos> findStructureStartingPoint(ServerLevel serverLevel, BlockPos pos, ResourceLocation location) {
        List<BlockPos> blockPosList = Lists.newArrayList();
        ResourceKey<Structure> key = RegistryUtil.keyStructure(location.toString());
        StructureStart start = serverLevel.structureManager()
                .getStructureAt(pos, Objects.requireNonNull(serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE).get(key)));
        if (start != StructureStart.INVALID_START && !start.getPieces().isEmpty()) {
            start.getPieces().forEach(piece -> {
                if (piece instanceof TemplateStructurePiece templateStructurePiece) {
                    blockPosList.add(templateStructurePiece.templatePosition());
                }
            });
        }
        return blockPosList;
    }

    public static void handleDataMarker(ServerLevel serverLevel, BlockPos pos, ResourceLocation location,MetadataHandler metadataHandler) {
        ResourceKey<Structure> key = RegistryUtil.keyStructure(location.toString());
        StructureStart start = serverLevel.structureManager()
                .getStructureAt(pos, Objects.requireNonNull(serverLevel.registryAccess().registryOrThrow(Registries.STRUCTURE).get(key)));
        if (start != StructureStart.INVALID_START && !start.getPieces().isEmpty()) {
            start.getPieces().forEach(piece -> {
                if (piece instanceof TemplateStructurePiece templateStructurePiece) {
                    StructureTemplate template = templateStructurePiece.template();
                    BlockPos startPos = templateStructurePiece.templatePosition();
                    StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(piece.getRotation()).setMirror(Mirror.NONE);
                    List<StructureTemplate.StructureBlockInfo> structureBlockInfos = template.filterBlocks(startPos, settings, Blocks.STRUCTURE_BLOCK);
                        structureBlockInfos.forEach(structureBlockInfo -> {
                        StructureMode structuremode = StructureMode.valueOf(structureBlockInfo.nbt().getString("mode"));
                        if (structuremode == StructureMode.DATA) {
                            String metadata = structureBlockInfo.nbt().getString("metadata");
                            metadataHandler.handleMetadata(serverLevel,metadata,structureBlockInfo,startPos);
                        }
                    });
                }
            });
        }
    }
    @FunctionalInterface
    public interface MetadataHandler {
        void handleMetadata(ServerLevel serverLevel, String metadata, StructureTemplate.StructureBlockInfo blockInfo, BlockPos startPos);
    }
}