package mod.surviving_the_aftermath.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

public class EventSubscriber {
	public static void onGatherData(GatherDataEvent event) {
		DataGenerator generator = event.getGenerator();
		PackOutput output = generator.getPackOutput();
		ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
		CompletableFuture<HolderLookup.Provider> provider = event.getLookupProvider();
		generator.addProvider(event.includeServer(), new ModTagProviders.ModBiomeTagProvider(output, provider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ModTagProviders.ModStructureTagProvider(output, provider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ModTagProviders.ModEntityTypeTagsProvider(output, provider, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModItemModelProvider(output, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModBlockStateProvider(output, existingFileHelper));
		generator.addProvider(event.includeClient(), new ModSoundProvider(output, existingFileHelper));
		generator.addProvider(event.includeServer(), new RegistryDataGenerator(output, provider));
		generator.addProvider(event.includeClient(), new ModLanguageProvider(output));
		generator.addProvider(event.includeClient(), new ModLanguageCNProvider(output));
		generator.addProvider(event.includeServer(), new ModLootTableProvider(output));
	}

}