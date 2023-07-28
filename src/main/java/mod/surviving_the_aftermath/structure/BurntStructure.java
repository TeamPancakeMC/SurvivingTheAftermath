package mod.surviving_the_aftermath.structure;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModStructurePieceTypes;
import mod.surviving_the_aftermath.init.ModStructureTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class BurntStructure extends AbstractStructure {

    private final int index;

    private static final Supplier<StructureType<?>>[] STRUCTURE_TYPES = new Supplier[] {null,
            ModStructureTypes.BURNT_1, ModStructureTypes.BURNT_2, ModStructureTypes.BURNT_3,
            ModStructureTypes.BURNT_4, ModStructureTypes.BURNT_5, ModStructureTypes.BURNT_6};

    private static final Supplier<StructurePieceType>[] STRUCTURE_PIECE_TYPES = new Supplier[] {null,
            ModStructurePieceTypes.BURNT_1, ModStructurePieceTypes.BURNT_2,
            ModStructurePieceTypes.BURNT_3, ModStructurePieceTypes.BURNT_4,
            ModStructurePieceTypes.BURNT_5, ModStructurePieceTypes.BURNT_6};

    public BurntStructure(StructureSettings settings, int index) {
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
        return Main.asResource("burnt_structure" + this.index);
    }

    public static class Piece extends AbstractStructure.Piece {

        public Piece(StructurePieceSerializationContext context, CompoundTag tag, int index) {
            super(BurntStructure.STRUCTURE_PIECE_TYPES[index].get(), context, tag);
        }

    }

}