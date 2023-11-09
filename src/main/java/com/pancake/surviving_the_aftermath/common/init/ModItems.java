package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {

	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, SurvivingTheAftermath.MOD_ID);
	public static final RegistryObject<Item> RAW_FALUKORV = ITEMS.register("raw_falukorv", () -> new Item(
			new Item.Properties().food((new FoodProperties.Builder()).nutrition(3).saturationMod(0.3F).build())));
	public static final RegistryObject<Item> COOKED_FALUKORV = ITEMS.register("cooked_falukorv", () -> new Item(
			new Item.Properties().food((new FoodProperties.Builder()).nutrition(10).saturationMod(14.0F)
					.effect(() -> new MobEffectInstance(MobEffects.DAMAGE_BOOST, 4800), 1.0F).build())));
	public static final RegistryObject<Item> EGG_TART = ITEMS.register("egg_tart", () -> new Item(
			new Item.Properties().food((new FoodProperties.Builder()).nutrition(3).saturationMod(2.0F)
					.effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 4800), 1.0F).build())));
	public static final RegistryObject<Item> STACK_OF_EGG_TARTS = ITEMS.register("stack_of_egg_tarts", () -> new Item(
			new Item.Properties().food((new FoodProperties.Builder()).nutrition(9).saturationMod(6.0F)
					.effect(() -> new MobEffectInstance(MobEffects.HEALTH_BOOST, 3600, 1), 1.0F)
					.effect(() -> new MobEffectInstance(MobEffects.CONFUSION, 60), 1.0F).build())));
	public static final RegistryObject<Item> HAMBURGER = ITEMS.register("hamburger", () -> new Item(
			new Item.Properties().food((new FoodProperties.Builder()).nutrition(7).saturationMod(4.0F)
					.effect(() -> new MobEffectInstance(MobEffects.REGENERATION, 6000), 1.0F).build())));
	public static final RegistryObject<Item> TIANJIN_PANCAKE = ITEMS.register("tianjin_pancake", () -> new Item(
			new Item.Properties().food((new FoodProperties.Builder()).nutrition(8).saturationMod(14.0F)
					.effect(() -> new MobEffectInstance(MobEffects.SATURATION, 4800), 1.0F)
					.effect(() -> new MobEffectInstance(MobEffects.LUCK, 2400), 1.0F).build())));
	public static final RegistryObject<Item> NETHER_CORE = ITEMS.register("nether_core", () -> new Item(new Item.Properties()));
	public static final RegistryObject<Item> MUSIC_DISK_ORCHELIAS_VOX = ITEMS.register("music_disk_orchelias_vox", () -> new RecordItem(
					15, ModSoundEvents.ORCHELIAS_VOX, (new Item.Properties()).stacksTo(1).rarity(Rarity.RARE), 269 * 20));

	static {
		ModBlocks.BLOCK_ITEMS.forEach(ITEMS::register);
	}

}