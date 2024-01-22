package com.pancake.surviving_the_aftermath.common.data.datagen;

import com.pancake.surviving_the_aftermath.common.data.datagen.raid.RaidModuleProvider;
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
		generator.addProvider(event.includeServer(), new ModTagProviders.ModBiomeTagsProvider(output, provider, existingFileHelper));
		generator.addProvider(event.includeServer(), new ModTagProviders.ModStructureTagsProvider(output, provider, existingFileHelper));
		generator.addProvider(event.includeServer(), new RaidModuleProvider(output));
		generator.addProvider(event.includeServer(), new RegistryDataGenerator(output, provider));

		generator.addProvider(event.includeClient(), new ModLanguageCNProvider(output));
		generator.addProvider(event.includeClient(), new ModLanguageProvider(output));

		generator.addProvider(event.includeClient(), new ModSoundProvider(output, existingFileHelper));
	}

}