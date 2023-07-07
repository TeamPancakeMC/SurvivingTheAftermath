package mod.surviving_the_aftermath.structure;

import java.util.Optional;

import com.mojang.serialization.Codec;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModStructurePieceTypes;
import mod.surviving_the_aftermath.init.ModStructureTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
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

		@SuppressWarnings("deprecation")
		@Override
		public void postProcess(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator pGenerator,
				RandomSource rand, BoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
			super.postProcess(pLevel, pStructureManager, pGenerator, rand, pBox, pChunkPos, pPos);
			var spawnPos = new BlockPos(rand.nextInt(pBox.minX(), pBox.maxX()), pPos.getY(),
					rand.nextInt(pBox.minZ(), pBox.maxZ()));
			for (int y = spawnPos.getY(); y < pBox.maxY(); y++) {
				if (getBlock(pLevel, spawnPos.getX(), y, spawnPos.getZ(), pBox).isAir()
						&& getBlock(pLevel, spawnPos.getX(), y + 1, spawnPos.getZ(), pBox).isAir()) {
					var villager = EntityType.VILLAGER.create(pLevel.getLevel());
					villager.moveTo(spawnPos.getX(), y, spawnPos.getZ());
					BuiltInRegistries.VILLAGER_TYPE.getRandom(rand).ifPresent((profession) -> {
						villager.setVillagerData(villager.getVillagerData().setType(profession.value()));
					});
					BuiltInRegistries.VILLAGER_PROFESSION.getRandom(rand).ifPresent((profession) -> {
						villager.setVillagerData(villager.getVillagerData().setProfession(profession.value()));
					});
					pLevel.addFreshEntity(villager);
					break;
				}
			}
		}
	}
}