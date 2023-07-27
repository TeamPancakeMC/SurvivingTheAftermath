package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.structure.CityStructure;
import mod.surviving_the_aftermath.structure.HouseOfSakura;
import mod.surviving_the_aftermath.structure.NetherRaidStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.Map;

public class ModStructures {
	
	public static final ResourceKey<Structure> CITY = ResourceKey.create(Registries.STRUCTURE, Main.asResource("city"));
	public static final ResourceKey<StructureSet> CITY_SET = ResourceKey.create(Registries.STRUCTURE_SET, Main.asResource("city"));
	public static final ResourceKey<Structure> HOUSE_OF_SAKURA = ResourceKey.create(Registries.STRUCTURE, Main.asResource("house_of_sakura"));
	public static final ResourceKey<StructureSet> HOUSE_OF_SAKURA_SET = ResourceKey.create(Registries.STRUCTURE_SET, Main.asResource("house_of_sakura"));
	public static final ResourceKey<Structure> NETHER_RAID = ResourceKey.create(Registries.STRUCTURE, Main.asResource("nether_raid"));
	public static final ResourceKey<StructureSet> NETHER_RAID_SET = ResourceKey.create(Registries.STRUCTURE_SET, Main.asResource("nether_raid"));

	public static void structureBootstrap(BootstapContext<Structure> context) {
		context.register(CITY, new CityStructure(new Structure.StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_CITY), Map.of(),
				GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_BOX)));
		context.register(HOUSE_OF_SAKURA, new HouseOfSakura(new Structure.StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_HOUSE_OF_SAKURA), Map.of(),
				GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN)));
		context.register(NETHER_RAID, new NetherRaidStructure(new Structure.StructureSettings(
				context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_NETHER_RAID), Map.of(),
				GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN)));
	}

	public static void structureSetBootstrap(BootstapContext<StructureSet> context) {
		context.register(CITY_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(CITY),
				new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 2057068235)));
		context.register(HOUSE_OF_SAKURA_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(HOUSE_OF_SAKURA),
				new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 1712650656)));
		context.register(NETHER_RAID_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(NETHER_RAID),
				new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 1629143766)));
	}

}