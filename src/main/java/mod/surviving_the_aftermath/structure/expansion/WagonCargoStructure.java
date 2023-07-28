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

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class WagonCargoStructure extends AbstractStructure {

    private final int index;

    private static final Supplier<StructureType<?>>[] STRUCTURE_TYPES = new Supplier[] {null,
            ModStructureTypes.WAGON_CARGO_1, ModStructureTypes.WAGON_CARGO_2, ModStructureTypes.WAGON_CARGO_3,
            ModStructureTypes.WAGON_CARGO_4, ModStructureTypes.WAGON_CARGO_5, ModStructureTypes.WAGON_CARGO_6};

    private static final Supplier<StructurePieceType>[] STRUCTURE_PIECE_TYPES = new Supplier[] {null,
            ModStructurePieceTypes.WAGON_CARGO_1, ModStructurePieceTypes.WAGON_CARGO_2,
            ModStructurePieceTypes.WAGON_CARGO_3, ModStructurePieceTypes.WAGON_CARGO_4,
            ModStructurePieceTypes.WAGON_CARGO_5, ModStructurePieceTypes.WAGON_CARGO_6};

    public WagonCargoStructure(StructureSettings settings, int index) {
        super(settings);
        this.index = index;
    }

    @Override
    public StructureType<?> type() {
        return STRUCTURE_TYPES[this.index].get();
    }

    @Override
    public StructurePieceType pieceType() {
        return STRUCTURE_PIECE_TYPES[this.index].get();
    }

    @Override
    public ResourceLocation location() {
        return Main.asResource("wagon_cargo" + this.index);
    }

    public static class Piece extends AbstractStructure.Piece {

        public Piece(StructurePieceSerializationContext context, CompoundTag tag, int index) {
            super(WagonCargoStructure.STRUCTURE_PIECE_TYPES[index].get(), context, tag);
        }

    }

}