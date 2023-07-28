package mod.surviving_the_aftermath.structure.expansion;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModStructurePieceTypes;
import mod.surviving_the_aftermath.init.ModStructureTypes;
import mod.surviving_the_aftermath.structure.AbstractStructure;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class ConstructionStructure extends AbstractStructure {

    private final int index;

    public ConstructionStructure(StructureSettings settings, int index) {
        super(settings);
        this.index = index;
    }

    @Override
    public StructureType<?> type() {
        return this.index == 1 ? ModStructureTypes.CONSTRUCTION_1.get() : ModStructureTypes.CONSTRUCTION_2.get();
    }

    @Override
    public StructurePieceType pieceType() {
        return this.index == 1 ? ModStructurePieceTypes.CONSTRUCTION_1.get() : ModStructurePieceTypes.CONSTRUCTION_2.get();
    }

    @Override
    public ResourceLocation location() {
        return Main.asResource("construction" + this.index);
    }

    public static class Piece extends AbstractStructure.Piece {

        public Piece(StructurePieceSerializationContext context, CompoundTag tag, int index) {
            super(index == 1 ? ModStructurePieceTypes.CONSTRUCTION_1.get() : ModStructurePieceTypes.CONSTRUCTION_2.get(), context, tag);
        }

    }

}