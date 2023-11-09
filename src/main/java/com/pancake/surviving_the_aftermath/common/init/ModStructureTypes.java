package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.structure.BurntStructure;
import com.pancake.surviving_the_aftermath.common.structure.CityStructure;
import com.pancake.surviving_the_aftermath.common.structure.HouseOfSakura;
import com.pancake.surviving_the_aftermath.common.structure.NetherRaidStructure;
import com.pancake.surviving_the_aftermath.common.structure.expansion.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModStructureTypes {

	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister.create(Registries.STRUCTURE_TYPE.location(), SurvivingTheAftermath.MOD_ID);
	public static final RegistryObject<StructureType<?>> HOUSE_OF_SAKURA = register("house_of_sakura", HouseOfSakura::new);
	public static final RegistryObject<StructureType<?>> NETHER_RAID = register("nether_raid", NetherRaidStructure::new);
	public static final RegistryObject<StructureType<?>> CITY = register("city", CityStructure::new);
	public static final RegistryObject<StructureType<?>> CAMP = register("camo", CampStructure::new);
	public static final RegistryObject<StructureType<?>> LOGS = register("logs", LogsStructure::new);
	public static final RegistryObject<StructureType<?>> TENT = register("tent", TentStructure::new);
	public static final RegistryObject<StructureType<?>> BRICK_WELL = register("brick_well", BrickWellStructure::new);
	public static final RegistryObject<StructureType<?>> COBBLESTONE_PILE = register("cobblestone_pile", CobblestonePileStructure::new);
	public static final RegistryObject<StructureType<?>> CONSTRUCTION_1 = register("construction1", (settings) -> new ConstructionStructure(settings, 1));
	public static final RegistryObject<StructureType<?>> CONSTRUCTION_2 = register("construction2", (settings) -> new ConstructionStructure(settings, 2));
	public static final RegistryObject<StructureType<?>> WAGON_CARGO_1 = register("wagon_cargo1", (settings) -> new WagonCargoStructure(settings, 1));
	public static final RegistryObject<StructureType<?>> WAGON_CARGO_2 = register("wagon_cargo2", (settings) -> new WagonCargoStructure(settings, 2));
	public static final RegistryObject<StructureType<?>> WAGON_CARGO_3 = register("wagon_cargo3", (settings) -> new WagonCargoStructure(settings, 3));
	public static final RegistryObject<StructureType<?>> WAGON_CARGO_4 = register("wagon_cargo4", (settings) -> new WagonCargoStructure(settings, 4));
	public static final RegistryObject<StructureType<?>> WAGON_CARGO_5 = register("wagon_cargo5", (settings) -> new WagonCargoStructure(settings, 5));
	public static final RegistryObject<StructureType<?>> WAGON_CARGO_6 = register("wagon_cargo6", (settings) -> new WagonCargoStructure(settings, 6));
	public static final RegistryObject<StructureType<?>> BURNT_1 = register("burnt_structure1", (settings) -> new BurntStructure(settings, 1));
	public static final RegistryObject<StructureType<?>> BURNT_2 = register("burnt_structure2", (settings) -> new BurntStructure(settings, 2));
	public static final RegistryObject<StructureType<?>> BURNT_3 = register("burnt_structure3", (settings) -> new BurntStructure(settings, 3));
	public static final RegistryObject<StructureType<?>> BURNT_4 = register("burnt_structure4", (settings) -> new BurntStructure(settings, 4));
	public static final RegistryObject<StructureType<?>> BURNT_5 = register("burnt_structure5", (settings) -> new BurntStructure(settings, 5));
	public static final RegistryObject<StructureType<?>> BURNT_6 = register("burnt_structure6", (settings) -> new BurntStructure(settings, 6));

	private static RegistryObject<StructureType<?>> register(String name, Function<Structure.StructureSettings, Structure> function) {
		return STRUCTURE_TYPES.register(name, () -> () -> Structure.simpleCodec(function));
	}

}