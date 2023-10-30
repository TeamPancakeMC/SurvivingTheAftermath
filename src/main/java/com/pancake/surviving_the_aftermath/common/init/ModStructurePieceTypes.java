package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.structure.*;
import com.pancake.surviving_the_aftermath.common.structure.expansion.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructurePieceTypes {

	public static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(Registries.STRUCTURE_PIECE.location(), SurvivingTheAftermath.MOD_ID);
	public static final RegistryObject<StructurePieceType> HOUSE_OF_SAKURA = register("house_of_sakura", HouseOfSakura.Piece::new);
	public static final RegistryObject<StructurePieceType> NETHER_RAID = register("nether_raid", NetherRaidStructure.Piece::new);
	public static final RegistryObject<StructurePieceType> CITY = register("city", CityStructure.Piece::new);
	public static final RegistryObject<StructurePieceType> CAMP = register("camp", CampStructure.Piece::new);
	public static final RegistryObject<StructurePieceType> LOGS = register("logs", LogsStructure.Piece::new);
	public static final RegistryObject<StructurePieceType> TENT = register("tent", TentStructure.Piece::new);
	public static final RegistryObject<StructurePieceType> BRICK_WELL = register("brick_well", BrickWellStructure.Piece::new);
	public static final RegistryObject<StructurePieceType> COBBLESTONE_PILE = register("cobblestone_pile", CobblestonePileStructure.Piece::new);
	public static final RegistryObject<StructurePieceType> CONSTRUCTION_1 = register("construction1", (context, tag) -> new ConstructionStructure.Piece(context, tag, 1));
	public static final RegistryObject<StructurePieceType> CONSTRUCTION_2 = register("construction2", (context, tag) -> new ConstructionStructure.Piece(context, tag, 2));
	public static final RegistryObject<StructurePieceType> WAGON_CARGO_1 = register("wagon_cargo1", (context, tag) -> new WagonCargoStructure.Piece(context, tag, 1));
	public static final RegistryObject<StructurePieceType> WAGON_CARGO_2 = register("wagon_cargo2", (context, tag) -> new WagonCargoStructure.Piece(context, tag, 2));
	public static final RegistryObject<StructurePieceType> WAGON_CARGO_3 = register("wagon_cargo3", (context, tag) -> new WagonCargoStructure.Piece(context, tag, 3));
	public static final RegistryObject<StructurePieceType> WAGON_CARGO_4 = register("wagon_cargo4", (context, tag) -> new WagonCargoStructure.Piece(context, tag, 4));
	public static final RegistryObject<StructurePieceType> WAGON_CARGO_5 = register("wagon_cargo5", (context, tag) -> new WagonCargoStructure.Piece(context, tag, 5));
	public static final RegistryObject<StructurePieceType> WAGON_CARGO_6 = register("wagon_cargo6", (context, tag) -> new WagonCargoStructure.Piece(context, tag, 6));
	public static final RegistryObject<StructurePieceType> BURNT_1 = register("burnt_structure1", (context, tag) -> new BurntStructure.Piece(context, tag, 1));
	public static final RegistryObject<StructurePieceType> BURNT_2 = register("burnt_structure2", (context, tag) -> new BurntStructure.Piece(context, tag, 2));
	public static final RegistryObject<StructurePieceType> BURNT_3 = register("burnt_structure3", (context, tag) -> new BurntStructure.Piece(context, tag, 3));
	public static final RegistryObject<StructurePieceType> BURNT_4 = register("burnt_structure4", (context, tag) -> new BurntStructure.Piece(context, tag, 4));
	public static final RegistryObject<StructurePieceType> BURNT_5 = register("burnt_structure5", (context, tag) -> new BurntStructure.Piece(context, tag, 5));
	public static final RegistryObject<StructurePieceType> BURNT_6 = register("burnt_structure6", (context, tag) -> new BurntStructure.Piece(context, tag, 6));

	private static RegistryObject<StructurePieceType> register(String name, StructurePieceType pieceType) {
		return STRUCTURE_PIECE_TYPES.register(name, () -> pieceType);
	}

}