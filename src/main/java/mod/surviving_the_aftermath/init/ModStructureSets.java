package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import static mod.surviving_the_aftermath.init.ModStructures.*;

public class ModStructureSets {

    public static final ResourceKey<StructureSet> HOUSE_OF_SAKURA_SET = register("house_of_sakura");
    public static final ResourceKey<StructureSet> NETHER_RAID_SET = register("nether_raid");
    public static final ResourceKey<StructureSet> CITY_SET = register("city");
    public static final ResourceKey<StructureSet> CAMP_SET = register("camp");
    public static final ResourceKey<StructureSet> LOGS_SET = register("logs");
    public static final ResourceKey<StructureSet> TENT_SET = register("tent");
    public static final ResourceKey<StructureSet> BRICK_WELL_SET = register("brick_well");
    public static final ResourceKey<StructureSet> CONSTRUCTION_1_SET = register("construction1");
    public static final ResourceKey<StructureSet> CONSTRUCTION_2_SET = register("construction2");
    public static final ResourceKey<StructureSet> COBBLESTONE_PILE_SET = register("cobblestone_pile");

    protected static ResourceKey<StructureSet> register(String name) {
        return ResourceKey.create(Registries.STRUCTURE_SET, Main.asResource(name));
    }

    public static void bootstrap(BootstapContext<StructureSet> context) {
        int[] wagonCargoSalt = new int[]{0, 1365330241, 161766032, 1733648479, 2081980804, 418595573, 947847921};
        int[] burntSalt = new int[] {0, 104706480, 705540248, 1838764692, 1669956657, 1334899266, 2119656745};
        context.register(CITY_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(CITY),
                new RandomSpreadStructurePlacement(80, 40, RandomSpreadType.LINEAR, 2057068235)));
        context.register(HOUSE_OF_SAKURA_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(HOUSE_OF_SAKURA),
                new RandomSpreadStructurePlacement(80, 40, RandomSpreadType.LINEAR, 1712650656)));
        context.register(NETHER_RAID_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(NETHER_RAID),
                new RandomSpreadStructurePlacement(100, 40, RandomSpreadType.LINEAR, 1629143766)));
        context.register(CAMP_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(CAMP),
                new RandomSpreadStructurePlacement(100, 40, RandomSpreadType.LINEAR, 1310135130)));
        context.register(LOGS_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(LOGS),
                new RandomSpreadStructurePlacement(90, 40, RandomSpreadType.LINEAR, 1321561321)));
        context.register(TENT_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(TENT),
                new RandomSpreadStructurePlacement(90, 40, RandomSpreadType.LINEAR, 1653821652)));
        context.register(BRICK_WELL_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(BRICK_WELL),
                new RandomSpreadStructurePlacement(95, 40, RandomSpreadType.LINEAR, 1561323135)));
        context.register(CONSTRUCTION_1_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(CONSTRUCTION_1),
                new RandomSpreadStructurePlacement(100, 40, RandomSpreadType.LINEAR, 761313131)));
        context.register(CONSTRUCTION_2_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(CONSTRUCTION_2),
                new RandomSpreadStructurePlacement(85, 40, RandomSpreadType.LINEAR, 498231431)));
        context.register(COBBLESTONE_PILE_SET, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(COBBLESTONE_PILE),
                new RandomSpreadStructurePlacement(75, 40, RandomSpreadType.LINEAR, 795416633)));
        for (int i = 1; i <= 6; i++) {
            ResourceKey<StructureSet> wagonCargoSet = register("wagon_cargo" + i);
            ResourceKey<StructureSet> burntSet = register("burnt_structure" + i);
            ResourceKey<Structure> wagonCargo = ModStructures.register("wagon_cargo" + i);
            ResourceKey<Structure> burnt = ModStructures.register("burnt_structure" + i);
            context.register(wagonCargoSet, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(wagonCargo),
                    new RandomSpreadStructurePlacement(90, 40, RandomSpreadType.LINEAR, wagonCargoSalt[i])));
            context.register(burntSet, new StructureSet(context.lookup(Registries.STRUCTURE).getOrThrow(burnt),
                    new RandomSpreadStructurePlacement(80, 40, RandomSpreadType.LINEAR, burntSalt[i])));
        }
    }

}
