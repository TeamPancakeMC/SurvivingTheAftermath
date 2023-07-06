package mod.surviving_the_aftermath.datagen;

import java.util.concurrent.CompletableFuture;

import org.jetbrains.annotations.Nullable;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBiomeTagProvider extends BiomeTagsProvider {

	public ModBiomeTagProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
			@Nullable ExistingFileHelper existingFileHelper) {
		super(output, lookupProvider, Main.MODID, existingFileHelper);
	}

	@Override
	protected void addTags(Provider pProvider) {
		tag(ModTags.HAS_CITY).add(Biomes.PLAINS);
		tag(ModTags.HAS_HOUSE_OF_SAKURA).add(Biomes.CHERRY_GROVE);
		tag(ModTags.HAS_NETHER_RAID).add(Biomes.PLAINS);
	}
}
