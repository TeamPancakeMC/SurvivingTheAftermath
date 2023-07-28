package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModBiomes;
import mod.surviving_the_aftermath.init.ModStructureSets;
import mod.surviving_the_aftermath.init.ModStructures;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.RegistrySetBuilder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.DatapackBuiltinEntriesProvider;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

public class RegistryDataGenerator extends DatapackBuiltinEntriesProvider {

    public static final RegistrySetBuilder BUILDER = new RegistrySetBuilder()
            .add(Registries.BIOME, ModBiomes::bootstrap)
            .add(Registries.STRUCTURE, ModStructures::bootstrap)
            .add(Registries.STRUCTURE_SET, ModStructureSets::bootstrap);

    public RegistryDataGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> provider) {
        super(output, provider, BUILDER, Set.of(Main.MODID));
    }

}