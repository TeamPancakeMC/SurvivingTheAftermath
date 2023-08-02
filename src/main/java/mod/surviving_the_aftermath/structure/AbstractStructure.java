package mod.surviving_the_aftermath.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.PiecesContainer;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;

import java.util.Optional;

public abstract class AbstractStructure extends Structure {

    public AbstractStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, (pieces) -> {
            ChunkPos chunk = context.chunkPos();
            WorldgenRandom rand = context.random();
            int x = chunk.getMinBlockX();
            int z = chunk.getMinBlockZ();
            int y = context.chunkGenerator().getFirstOccupiedHeight(
                    x, z, Heightmap.Types.WORLD_SURFACE_WG,
                    context.heightAccessor(), context.randomState());
            BlockPos pos = new BlockPos(x, y, z);
            Rotation rotation = Rotation.getRandom(rand);
            pieces.addPiece(new AbstractStructure.Piece(this.pieceType(), context.structureTemplateManager(), this.location(), pos, rotation));
        });
    }
    
	@Override
	public void afterPlace(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator pChunkGenerator,
			RandomSource pRandom, BoundingBox pBoundingBox, ChunkPos pChunkPos, PiecesContainer pPieces) {
		BlockPos.betweenClosedStream(pBoundingBox).forEach(p -> {
			if (pLevel.getBlockEntity(p) instanceof RandomizableContainerBlockEntity chest) {
				chest.setLootTable(BuiltInLootTables.DESERT_PYRAMID, pRandom.nextLong());
			}
		});
	}

    @Override
    public abstract StructureType<?> type();

    public abstract StructurePieceType pieceType();

    public abstract ResourceLocation location();

    public static class Piece extends TemplateStructurePiece {

        public Piece(StructurePieceType type, StructureTemplateManager structureTemplateManager, ResourceLocation location, BlockPos templatePosition, Rotation rotation) {
            super(type, 0, structureTemplateManager, location, location.toString(), makeSettings(rotation), templatePosition);
        }

        public Piece(StructurePieceType type, StructureTemplateManager structureManager, CompoundTag tag) {
            super(type, tag, structureManager, (location) -> makeSettings(Rotation.valueOf(tag.getString("rot"))));
        }

        public Piece(StructurePieceType type, StructurePieceSerializationContext context, CompoundTag tag) {
            this(type, context.structureTemplateManager(), tag);
        }

        @Override
        protected void handleDataMarker(String name, BlockPos pos, ServerLevelAccessor level, RandomSource random, BoundingBox box) {}

        @Override
        protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {
            super.addAdditionalSaveData(context, tag);
            tag.putString("rot", this.placeSettings.getRotation().name());
        }

        private static StructurePlaceSettings makeSettings(Rotation rotation) {
            return new StructurePlaceSettings().setRotation(rotation).setMirror(Mirror.NONE).addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
        }
        
    }
    
}