package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModStructures;
import mod.surviving_the_aftermath.init.ModTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class ModTagProviders {

	public static class ModBiomeTagProvider extends BiomeTagsProvider {

		public ModBiomeTagProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
				@Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, Main.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(Provider provider) {
			tag(ModTags.HAS_CITY).add(Biomes.PLAINS);
			tag(ModTags.HAS_HOUSE_OF_SAKURA).add(Biomes.CHERRY_GROVE);
			tag(ModTags.HAS_NETHER_RAID).add(Biomes.PLAINS);
			tag(ModTags.HAS_EXPANSION_BUILD).addTag(BiomeTags.IS_FOREST).add(Biomes.PLAINS);
			tag(ModTags.HAS_BURNT_STRUCTURE).addTag(BiomeTags.IS_BADLANDS).addTag(BiomeTags.HAS_DESERT_PYRAMID);
		}

	}

	public static class ModStructureTagProvider extends StructureTagsProvider {

		public ModStructureTagProvider(PackOutput output, CompletableFuture<Provider> provider,
				@Nullable ExistingFileHelper existingFileHelper) {
			super(output, provider, Main.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(Provider provider) {
			tag(ModTags.NETHER_RAID).addOptional(ModStructures.NETHER_RAID.location());
		}

	}

}