package com.pancake.surviving_the_aftermath.common.data.datagen;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
import com.pancake.surviving_the_aftermath.common.init.ModTags;
import net.minecraft.core.HolderLookup;
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
    public static class ModBiomeTagsProvider extends BiomeTagsProvider {

        public ModBiomeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                    @Nullable ExistingFileHelper existingFileHelper) {
            super(output, lookupProvider, SurvivingTheAftermath.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(ModTags.HAS_CITY).add(Biomes.PLAINS);
            tag(ModTags.HAS_NETHER_RAID).add(Biomes.PLAINS);
            tag(ModTags.HAS_HOUSE_OF_SAKURA).add(Biomes.CHERRY_GROVE);
            tag(ModTags.HAS_EXPANSION_BUILD).addTag(BiomeTags.IS_FOREST).add(Biomes.PLAINS);
            tag(ModTags.HAS_BURNT_STRUCTURE).addTag(BiomeTags.IS_BADLANDS).addTag(BiomeTags.HAS_DESERT_PYRAMID);
        }

    }

    public static class ModStructureTagsProvider extends StructureTagsProvider {

        public ModStructureTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> provider,
                                        @Nullable ExistingFileHelper existingFileHelper) {
            super(output, provider, SurvivingTheAftermath.MOD_ID, existingFileHelper);
        }

        @Override
        protected void addTags(HolderLookup.Provider provider) {
            tag(ModTags.NETHER_RAID).addOptional(ModStructures.NETHER_RAID.location());
        }

    }
}