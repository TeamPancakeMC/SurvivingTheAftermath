package com.pancake.surviving_the_aftermath.common.data.datagen;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.init.ModEnchantments;
import com.pancake.surviving_the_aftermath.common.init.ModItems;
import com.pancake.surviving_the_aftermath.common.init.ModMobEffects;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

	public ModLanguageProvider(PackOutput output) {
		super(output, SurvivingTheAftermath.MOD_ID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("itemGroup." + SurvivingTheAftermath.MOD_ID, "Surviving the Aftermath");
		add(ModItems.RAW_FALUKORV.get(), "Raw Falukorv");
		add(ModItems.EGG_TART.get(), "Egg Tart");
		add(ModItems.STACK_OF_EGG_TARTS.get(), "Stack of Egg Tart");
		add(ModItems.HAMBURGER.get(), "Hamburger");
		add(ModItems.TIANJIN_PANCAKE.get(), "Tianjin Pancake");
		add(ModItems.NETHER_CORE.get(), "Nether Core");
		add(ModItems.MUSIC_DISK_ORCHELIAS_VOX.get(), "Music Disc");
		add(ModItems.MUSIC_DISK_ORCHELIAS_VOX.get().getDescriptionId() + ".desc",
				"Hagali - Orchelia's vox (offvocal ver_)");
		add(ModMobEffects.COWARDICE.get(),"Cowardice");
		add(ModEnchantments.COUNTER_ATTACK.get(), "Counter Attack");
		add(ModEnchantments.BLOODTHIRSTY.get(), "Bloodthirsty");
		add(ModEnchantments.CLEAN_WATER.get(), "Clean Water");
		add(ModEnchantments.LIFE_TREE.get(), "Life Tree");
		add(ModEnchantments.DEVOURED.get(), "Devoured");
		add(ModEnchantments.FRANTIC.get(), "Frantic");
		add(ModEnchantments.EXECUTE.get(), "Execute");
		add(ModEnchantments.RANGER.get(), "Ranger");
		add(ModEnchantments.MOON.get(), "Moon");
		add(ModEnchantments.SUN.get(), "Sun");
	}

}