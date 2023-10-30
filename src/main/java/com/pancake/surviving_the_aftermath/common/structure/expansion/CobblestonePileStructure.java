package com.pancake.surviving_the_aftermath.common.structure.expansion;

import com.pancake.surviving_the_aftermath.common.init.ModStructurePieceTypes;
import com.pancake.surviving_the_aftermath.common.init.ModStructureTypes;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.structure.AbstractStructure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class CobblestonePileStructure extends AbstractStructure {

    public CobblestonePileStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    public StructureType<?> type() {
        return ModStructureTypes.COBBLESTONE_PILE.get();
    }

    @Override
    public StructurePieceType pieceType() {
        return ModStructurePieceTypes.COBBLESTONE_PILE.get();
    }

    @Override
    public ResourceLocation location() {
        return SurvivingTheAftermath.asResource("cobblestone_pile");
    }

    public static class Piece extends AbstractStructure.Piece {

        public Piece(StructurePieceSerializationContext context, CompoundTag tag) {
            super(ModStructurePieceTypes.COBBLESTONE_PILE.get(), context, tag);
        }

    }

}