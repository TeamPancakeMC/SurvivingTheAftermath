package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureSet;

public class ModStructures {
	public static final ResourceKey<Structure> CITY = ResourceKey.create(Registries.STRUCTURE,
			new ResourceLocation(Main.MODID, "city"));
	public static final ResourceKey<StructureSet> CITY_SET = ResourceKey.create(Registries.STRUCTURE_SET,
			new ResourceLocation(Main.MODID, "city"));

	public static final ResourceKey<Structure> HOUSE_OF_SAKURA = ResourceKey.create(Registries.STRUCTURE,
			new ResourceLocation(Main.MODID, "house_of_sakura"));
	public static final ResourceKey<StructureSet> HOUSE_OF_SAKURA_SET = ResourceKey.create(Registries.STRUCTURE_SET,
			new ResourceLocation(Main.MODID, "house_of_sakura"));
	
	public static final ResourceKey<Structure> NETHER_RAID = ResourceKey.create(Registries.STRUCTURE,
			new ResourceLocation(Main.MODID, "nether_raid"));
	public static final ResourceKey<StructureSet> NETHER_RAID_SET = ResourceKey.create(Registries.STRUCTURE_SET,
			new ResourceLocation(Main.MODID, "nether_raid"));
}
