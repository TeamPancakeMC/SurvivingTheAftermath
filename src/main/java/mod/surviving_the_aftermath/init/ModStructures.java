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
}
