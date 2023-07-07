package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;

public class ModTags {
	public static final TagKey<Biome> HAS_CITY = TagKey.create(Registries.BIOME,
			new ResourceLocation(Main.MODID, "has_city"));
	public static final TagKey<Biome> HAS_HOUSE_OF_SAKURA = TagKey
			.create(Registries.BIOME,
			new ResourceLocation(Main.MODID, "has_house_of_sakura"));
	public static final TagKey<Biome> HAS_NETHER_RAID = TagKey
			.create(Registries.BIOME,
			new ResourceLocation(Main.MODID, "has_nether_raid"));
	public static final TagKey<Structure> NETHER_RAID = TagKey
			.create(Registries.STRUCTURE,
					new ResourceLocation(Main.MODID, "nether_raid"));
}
