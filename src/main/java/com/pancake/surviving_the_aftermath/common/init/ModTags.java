package com.pancake.surviving_the_aftermath.common.init;


import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.structure.Structure;
public class ModTags {

	public static final TagKey<Biome> HAS_CITY = createTag(Registries.BIOME, "has_city");
	public static final TagKey<Biome> HAS_HOUSE_OF_SAKURA = createTag(Registries.BIOME, "has_house_of_sakura");
	public static final TagKey<Biome> HAS_NETHER_RAID = createTag(Registries.BIOME, "has_nether_raid");
	public static final TagKey<Biome> HAS_EXPANSION_BUILD = createTag(Registries.BIOME, "has_expansion_build");
	public static final TagKey<Biome> HAS_BURNT_STRUCTURE = createTag(Registries.BIOME, "has_burnt_structure");
	public static final TagKey<Structure> NETHER_RAID = createTag(Registries.STRUCTURE, "nether_raid");
	public static final TagKey<EntityType<?>> NETHER_MOB = createTag(Registries.ENTITY_TYPE, "nether_mob");

	private static <T> TagKey<T> createTag(ResourceKey<? extends Registry<T>> resourceKey, String name) {
		return TagKey.create(resourceKey, SurvivingTheAftermath.asResource(name));
	}

}