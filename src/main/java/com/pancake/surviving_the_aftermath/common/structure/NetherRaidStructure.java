package com.pancake.surviving_the_aftermath.common.structure;


import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import com.pancake.surviving_the_aftermath.common.init.ModStructurePieceTypes;
import com.pancake.surviving_the_aftermath.common.init.ModStructureTypes;

public class NetherRaidStructure extends AbstractStructure {


	public static final ResourceLocation STRUCTURE_TRANSFORMED = SurvivingTheAftermath.asResource("nether_invasion_portal_transformed");

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
		return SurvivingTheAftermath.asResource("nether_invasion_portal");
	}

	public static class Piece extends AbstractStructure.Piece {

		public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
			super(ModStructurePieceTypes.NETHER_RAID.get(), context.structureTemplateManager(), tag);
		}

	}

}