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
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

public class NetherRaidStructure extends Structure {

	public static final Codec<Structure> CODEC = simpleCodec(NetherRaidStructure::new);
	private static final ResourceLocation STRUCTURE = new ResourceLocation(Main.MODID, "nether_invasion_portal");

	public NetherRaidStructure(StructureSettings settings) {
		super(settings);
	}

	@Override
	public Optional<Structure.GenerationStub> findGenerationPoint(Structure.GenerationContext pContext) {
		return onTopOfChunkCenter(pContext, Heightmap.Types.WORLD_SURFACE_WG, (pieces) -> {
			var chunk = pContext.chunkPos();
			var rand = pContext.random();
			var x = chunk.getMinBlockX();
			var z = chunk.getMinBlockZ();
			var y = pContext.chunkGenerator().getFirstOccupiedHeight(x, z, Heightmap.Types.WORLD_SURFACE_WG,
					pContext.heightAccessor(), pContext.randomState());
			var pos = new BlockPos(x, y, z);
			var rotation = Rotation.getRandom(rand);
			pieces.addPiece(new Piece(pContext.structureTemplateManager(), STRUCTURE, pos, rotation));
		});
	}

	@Override
	public StructureType<?> type() {
		return ModStructureTypes.NETHER_RAID.get();
	}

	public static class Piece extends TemplateStructurePiece {
		public Piece(StructureTemplateManager pStructureManager, ResourceLocation pLocation, BlockPos pPos,
				Rotation pRotation) {
			super(ModStructurePieceTypes.NETHER_RAID.get(), 0, pStructureManager, pLocation, pLocation.toString(),
					makeSettings(pRotation), pPos);
		}

		public Piece(StructureTemplateManager pStructureManager, CompoundTag pTag) {
			super(ModStructurePieceTypes.NETHER_RAID.get(), pTag, pStructureManager, (p_228568_) -> {
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