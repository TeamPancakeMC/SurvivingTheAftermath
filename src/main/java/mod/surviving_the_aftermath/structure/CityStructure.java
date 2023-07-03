package mod.surviving_the_aftermath.structure;

import java.util.Optional;

import com.mojang.serialization.Codec;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModStructurePieceTypes;
import mod.surviving_the_aftermath.init.ModStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class CityStructure extends Structure {

	public static final Codec<Structure> CODEC = simpleCodec(CityStructure::new);
	private static final ResourceLocation STRUCTURE = new ResourceLocation(Main.MODID, "city");

	public CityStructure(StructureSettings settings) {
		super(settings);
	}

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
		return onTopOfChunkCenter(pContext, Heightmap.Types.WORLD_SURFACE_WG, (pieces) -> {
			var chunk = pContext.chunkPos();
			var rand = pContext.random();
			var pos = new BlockPos(chunk.getMinBlockX(), 90, chunk.getMinBlockZ());
			var rotation = Rotation.getRandom(rand);
			pieces.addPiece(new Piece(pContext.structureTemplateManager(), STRUCTURE, pos, rotation));
		});
	}

	@Override
	public StructureType<?> type() {
		return ModStructureTypes.CITY.get();
	}

	public static class Piece extends TemplateStructurePiece {
		public Piece(StructureTemplateManager pStructureManager, ResourceLocation pLocation, BlockPos pPos,
				Rotation pRotation) {
			super(ModStructurePieceTypes.CITY.get(), 0, pStructureManager, pLocation, pLocation.toString(),
					makeSettings(pRotation), pPos);
		}

		public Piece(StructureTemplateManager pStructureManager, CompoundTag pTag) {
			super(ModStructurePieceTypes.CITY.get(), pTag, pStructureManager, (p_228568_) -> {
				return makeSettings(Rotation.valueOf(pTag.getString("rot")));
			});
		}

		public Piece(StructurePieceSerializationContext pContext, CompoundTag pTag) {
			this(pContext.structureTemplateManager(), pTag);
		}

		private static StructurePlaceSettings makeSettings(Rotation pRotation) {
			return new StructurePlaceSettings().setRotation(pRotation).setMirror(Mirror.NONE)
					.addProcessor(BlockIgnoreProcessor.STRUCTURE_AND_AIR);
		}

		@Override
		protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag pTag) {
			super.addAdditionalSaveData(pContext, pTag);
			pTag.putString("rot", this.placeSettings.getRotation().name());
		}

		@Override
		protected void handleDataMarker(String pName, BlockPos pPos, ServerLevelAccessor pLevel, RandomSource pRandom,
				BoundingBox pBox) {

		}
	}
}