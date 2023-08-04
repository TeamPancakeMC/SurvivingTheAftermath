package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModItems;
import mod.surviving_the_aftermath.init.ModStructures;
import mod.surviving_the_aftermath.init.ModTags;
import net.minecraft.core.HolderLookup.Provider;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.data.tags.StructureTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.concurrent.CompletableFuture;

@ParametersAreNonnullByDefault
public class ModTagProviders {

	public static class ModBlockTagsProvider extends BlockTagsProvider {

		public ModBlockTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
				@Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, Main.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(Provider pProvider) {

		}

	}

	public static class ModItemTagsProvider extends ItemTagsProvider {

		public ModItemTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
				CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, blockTags, Main.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(Provider provider) {
			this.tag(ItemTags.MUSIC_DISCS).add(ModItems.MUSIC_DISK_ORCHELIAS_VOX.get());
		}

	}

	public static class ModBiomeTagsProvider extends BiomeTagsProvider {

		public ModBiomeTagsProvider(PackOutput output, CompletableFuture<Provider> lookupProvider,
				@Nullable ExistingFileHelper existingFileHelper) {
			super(output, lookupProvider, Main.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(Provider provider) {
			tag(ModTags.HAS_CITY).add(Biomes.PLAINS);
			tag(ModTags.HAS_NETHER_RAID).add(Biomes.PLAINS);
			tag(ModTags.HAS_HOUSE_OF_SAKURA).add(Biomes.CHERRY_GROVE);
			tag(ModTags.HAS_EXPANSION_BUILD).addTag(BiomeTags.IS_FOREST).add(Biomes.PLAINS);
			tag(ModTags.HAS_BURNT_STRUCTURE).addTag(BiomeTags.IS_BADLANDS).addTag(BiomeTags.HAS_DESERT_PYRAMID);
		}

	}

	public static class ModStructureTagsProvider extends StructureTagsProvider {

		public ModStructureTagsProvider(PackOutput output, CompletableFuture<Provider> provider,
				@Nullable ExistingFileHelper existingFileHelper) {
			super(output, provider, Main.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(Provider provider) {
			tag(ModTags.NETHER_RAID).addOptional(ModStructures.NETHER_RAID.location());
		}

	}

	public static class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {

		public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<Provider> provider,
				@Nullable ExistingFileHelper existingFileHelper) {
			super(output, provider, Main.MODID, existingFileHelper);
		}

		@Override
		protected void addTags(Provider provider) {
			this.tag(ModTags.NETHER_MOB).add(EntityType.ZOMBIFIED_PIGLIN, EntityType.GHAST, EntityType.MAGMA_CUBE,
					EntityType.BLAZE, EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.ENDERMAN,
					EntityType.HOGLIN, EntityType.PIGLIN, EntityType.PIGLIN_BRUTE, EntityType.STRIDER);
		}

	}

}