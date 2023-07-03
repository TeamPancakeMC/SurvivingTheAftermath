package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.structure.CityStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructurePieceTypes {
	public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister
			.create(Registries.STRUCTURE_PIECE.location(), Main.MODID);

	public static final RegistryObject<StructurePieceType> CITY = STRUCTURE_PIECE_TYPES.register("city",
			() -> CityStructure.Piece::new);
}
