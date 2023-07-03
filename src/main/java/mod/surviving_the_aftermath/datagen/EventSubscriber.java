package mod.surviving_the_aftermath.datagen;

import java.util.Map;
import java.util.Set;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModStructures;
import mod.surviving_the_aftermath.init.ModTags;
import mod.surviving_the_aftermath.structure.CityStructure;
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

		generator.addProvider(event.includeServer(),
				new ModBiomeTagProvider(generator.getPackOutput(), event.getLookupProvider(), existingFileHelper));
		generator.addProvider(event.includeServer(),
				(DataProvider.Factory<DatapackBuiltinEntriesProvider>) o -> new DatapackBuiltinEntriesProvider(o,
						event.getLookupProvider(), new RegistrySetBuilder().add(Registries.STRUCTURE, context -> {
							context.register(ModStructures.CITY,
									new CityStructure(new StructureSettings(
											context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_CITY), Map.of(),
											GenerationStep.Decoration.SURFACE_STRUCTURES,
											TerrainAdjustment.BEARD_BOX)));
						}).add(Registries.STRUCTURE_SET, context -> {
							context.register(ModStructures.CITY_SET, new StructureSet(
									context.lookup(Registries.STRUCTURE).getOrThrow(ModStructures.CITY),
									new RandomSpreadStructurePlacement(32, 8, RandomSpreadType.LINEAR, 2057068235)));
						}), Set.of(Main.MODID)));

		generator.addProvider(event.includeClient(), new ModLanguageProvider(output));
		generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModSoundProvider(output, existingFileHelper));
	}
}
