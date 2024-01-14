package com.pancake.surviving_the_aftermath.common.structure.expansion;


import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.init.ModStructurePieceTypes;
import com.pancake.surviving_the_aftermath.common.init.ModStructureTypes;
import com.pancake.surviving_the_aftermath.common.structure.AbstractStructure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class BrickWellStructure extends AbstractStructure {

    public BrickWellStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.BRICK_WELL.get();
    }

    @Override
    public StructurePieceType pieceType() {
        return ModStructurePieceTypes.BRICK_WELL.get();
    }

    @Override
    public ResourceLocation location() {
        return SurvivingTheAftermath.asResource("brick_well");
    }

    public static class Piece extends AbstractStructure.Piece {

        public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
            super(ModStructurePieceTypes.BRICK_WELL.get(), context, tag);
        }

    }

}