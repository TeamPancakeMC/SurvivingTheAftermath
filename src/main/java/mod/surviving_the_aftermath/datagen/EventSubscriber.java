package mod.surviving_the_aftermath.datagen;

import java.util.Map;
import java.util.Set;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModStructures;
import mod.surviving_the_aftermath.init.ModTags;
import mod.surviving_the_aftermath.structure.CityStructure;
import mod.surviving_the_aftermath.structure.HouseOfSakura;
import mod.surviving_the_aftermath.structure.NetherRaidStructure;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.DataProvider;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure.StructureSettings;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(bus = Bus.MOD, modid = Main.MODID)
public class EventSubscriber {

	@SubscribeEvent
	public static void onGatherData(GatherDataEvent event) {
		var generator = event.getGenerator();
		var output = generator.getPackOutput();
		var existingFileHelper = event.getExistingFileHelper();

		generator.addProvider(event.includeServer(), new ModTagProviders.ModBiomeTagProvider(generator.getPackOutput(),
				event.getLookupProvider(), existingFileHelper));
		generator.addProvider(event.includeServer(), new ModTagProviders.ModStructureTagProvider(
				generator.getPackOutput(), event.getLookupProvider(), existingFileHelper));
		generator.addProvider(event.includeServer(),
				(DataProvider.Factory<DatapackBuiltinEntriesProvider>) o -> new DatapackBuiltinEntriesProvider(o,
						event.getLookupProvider(), new RegistrySetBuilder().add(Registries.STRUCTURE, context -> {
							context.register(ModStructures.CITY,
									new CityStructure(new StructureSettings(
											context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_CITY), Map.of(),
											GenerationStep.Decoration.SURFACE_STRUCTURES,
											TerrainAdjustment.BEARD_BOX)));
							context.register(ModStructures.HOUSE_OF_SAKURA,
									new HouseOfSakura(new StructureSettings(
											context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_HOUSE_OF_SAKURA),
											Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES,
											TerrainAdjustment.BEARD_THIN)));
							context.register(ModStructures.NETHER_RAID,
									new NetherRaidStructure(new StructureSettings(
											context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_NETHER_RAID),
											Map.of(), GenerationStep.Decoration.SURFACE_STRUCTURES,
											TerrainAdjustment.BEARD_THIN)));
						}).add(Registries.STRUCTURE_SET, context -> {
							context.register(ModStructures.CITY_SET, new StructureSet(
									context.lookup(Registries.STRUCTURE).getOrThrow(ModStructures.CITY),
									new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 2057068235)));
							context.register(ModStructures.HOUSE_OF_SAKURA_SET, new StructureSet(
									context.lookup(Registries.STRUCTURE).getOrThrow(ModStructures.HOUSE_OF_SAKURA),
									new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 1712650656)));
							context.register(ModStructures.NETHER_RAID_SET, new StructureSet(
									context.lookup(Registries.STRUCTURE).getOrThrow(ModStructures.NETHER_RAID),
									new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 1629143766)));

						}), Set.of(Main.MODID)));

		generator.addProvider(event.includeClient(), new ModLanguageProvider(output));
		generator.addProvider(event.includeClient(), new ModLanguageCNProvider(output));
		generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModSoundProvider(output, existingFileHelper));
	}
}
