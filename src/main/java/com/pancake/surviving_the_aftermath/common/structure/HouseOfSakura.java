package com.pancake.surviving_the_aftermath.common.structure;


import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.init.ModStructurePieceTypes;
import com.pancake.surviving_the_aftermath.common.init.ModStructureTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class HouseOfSakura extends AbstractStructure {

	public HouseOfSakura(StructureSettings settings) {
		super(settings);
	}

	@Override
	public StructureType<?> type() {
		return ModStructureTypes.HOUSE_OF_SAKURA.get();
	}

	@Override
	public StructurePieceType pieceType() {
		return ModStructurePieceTypes.HOUSE_OF_SAKURA.get();
	}

	@Override
	public ResourceLocation location() {
		return SurvivingTheAftermath.asResource("house_of_sakura");
	}

	public static class Piece extends AbstractStructure.Piece {

		public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
			super(ModStructurePieceTypes.HOUSE_OF_SAKURA.get(), context.structureTemplateManager(), tag);
		}

	}

}