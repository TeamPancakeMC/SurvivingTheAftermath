package mod.surviving_the_aftermath.structure;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModStructurePieceTypes;
import mod.surviving_the_aftermath.init.ModStructureTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class NetherRaidStructure extends AbstractStructure {

	public static final ResourceLocation STRUCTURE_TRANSFORMED = new ResourceLocation(Main.MODID,
			"nether_invasion_portal_transformed");

	public NetherRaidStructure(StructureSettings settings) {
		super(settings);
	}

	@Override
	public StructureType<?> type() {
		return ModStructureTypes.NETHER_RAID.get();
	}

	@Override
	public StructurePieceType pieceType() {
		return ModStructurePieceTypes.NETHER_RAID.get();
	}

	@Override
	public ResourceLocation location() {
		return Main.asResource("nether_invasion_portal");
	}

	public static class Piece extends AbstractStructure.Piece {

		public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
			super(ModStructurePieceTypes.NETHER_RAID.get(), context.structureTemplateManager(), tag);
		}

	}

}