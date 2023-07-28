package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModTags {

	public static final TagKey<Biome> HAS_CITY = TagKey.create(Registries.BIOME, new ResourceLocation(Main.MODID, "has_city"));
	public static final TagKey<Biome> HAS_HOUSE_OF_SAKURA = TagKey.create(Registries.BIOME, Main.asResource("has_house_of_sakura"));
	public static final TagKey<Biome> HAS_NETHER_RAID = TagKey.create(Registries.BIOME, Main.asResource("has_nether_raid"));
	public static final TagKey<Biome> HAS_EXPANSION_BUILD = TagKey.create(Registries.BIOME, Main.asResource("has_expansion_build"));
	public static final TagKey<Biome> HAS_BURNT_STRUCTURE = TagKey.create(Registries.BIOME, Main.asResource("has_burnt_structure"));
	public static final TagKey<Structure> NETHER_RAID = TagKey.create(Registries.STRUCTURE, Main.asResource("nether_raid"));

}