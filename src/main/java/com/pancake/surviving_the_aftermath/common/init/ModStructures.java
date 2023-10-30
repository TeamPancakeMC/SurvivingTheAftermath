package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.structure.BurntStructure;
import com.pancake.surviving_the_aftermath.common.structure.CityStructure;
import com.pancake.surviving_the_aftermath.common.structure.HouseOfSakura;
import com.pancake.surviving_the_aftermath.common.structure.NetherRaidStructure;
import com.pancake.surviving_the_aftermath.common.structure.expansion.*;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.TerrainAdjustment;

import java.util.Map;

public class ModStructures {
    public static final ResourceKey<Structure> HOUSE_OF_SAKURA = register("house_of_sakura");
    public static final ResourceKey<Structure> NETHER_RAID = register("nether_raid");
    public static final ResourceKey<Structure> CITY = register("city");
    public static final ResourceKey<Structure> CAMP = register("camp");
    public static final ResourceKey<Structure> LOGS = register("logs");
    public static final ResourceKey<Structure> TENT = register("tent");
    public static final ResourceKey<Structure> BRICK_WELL = register("brick_well");
    public static final ResourceKey<Structure> CONSTRUCTION_1 = register("construction1");
    public static final ResourceKey<Structure> CONSTRUCTION_2 = register("construction2");
    public static final ResourceKey<Structure> COBBLESTONE_PILE = register("cobblestone_pile");

    protected static ResourceKey<Structure> register(String name) {
        return ResourceKey.create(Registries.STRUCTURE, SurvivingTheAftermath.asResource(name));
    }

    public static void bootstrap(BootstapContext<Structure> context) {
        Structure.StructureSettings expansionBuildSettings = new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_EXPANSION_BUILD), Map.of(),
                GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);
        Structure.StructureSettings burntStructureSettings = new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_BURNT_STRUCTURE), Map.of(),
                GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.NONE);
        context.register(CITY, new CityStructure(new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_CITY), Map.of(),
                GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_BOX)));
        context.register(HOUSE_OF_SAKURA, new HouseOfSakura(new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_HOUSE_OF_SAKURA), Map.of(),
                GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN)));
        context.register(NETHER_RAID, new NetherRaidStructure(new Structure.StructureSettings(
                context.lookup(Registries.BIOME).getOrThrow(ModTags.HAS_NETHER_RAID), Map.of(),
                GenerationStep.Decoration.SURFACE_STRUCTURES, TerrainAdjustment.BEARD_THIN)));
        context.register(CAMP, new CampStructure(expansionBuildSettings));
        context.register(LOGS, new LogsStructure(expansionBuildSettings));
        context.register(TENT, new TentStructure(expansionBuildSettings));
        context.register(BRICK_WELL, new BrickWellStructure(expansionBuildSettings));
        context.register(CONSTRUCTION_1, new ConstructionStructure(expansionBuildSettings, 1));
        context.register(CONSTRUCTION_2, new ConstructionStructure(expansionBuildSettings, 2));
        context.register(COBBLESTONE_PILE, new CobblestonePileStructure(expansionBuildSettings));
        for (int i = 1; i <= 6; i++) {
            ResourceKey<Structure> wagonCargo = register("wagon_cargo" + i);
            ResourceKey<Structure> burnt = register("burnt_structure" + i);
            context.register(wagonCargo, new WagonCargoStructure(expansionBuildSettings, i));
            context.register(burnt, new BurntStructure(burntStructureSettings, i));
        }
    }
}
