package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModBlocks;
import mod.surviving_the_aftermath.init.ModEnchantments;
import mod.surviving_the_aftermath.init.ModItems;
import mod.surviving_the_aftermath.init.ModMobEffects;
import mod.surviving_the_aftermath.raid.NetherRaid;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageProvider extends LanguageProvider {

	public ModLanguageProvider(PackOutput output) {
		super(output, Main.MODID, "en_us");
	}

	@Override
	protected void addTranslations() {
		add("itemGroup." + Main.MODID, "Surviving the Aftermath");
		add(ModBlocks.PARTY_BONFIRE.get(), "Party Bonfire");
		add(ModItems.RAW_FALUKORV.get(), "Raw Falukorv");
		add(ModItems.EGG_TART.get(), "Egg Tart");
		add(ModItems.STACK_OF_EGG_TARTS.get(), "Stack of Egg Tart");
		add(ModItems.HAMBURGER.get(), "Hamburger");
		add(ModItems.TIANJIN_PANCAKE.get(), "Tianjin Pancake");
		add(ModItems.NETHER_CORE.get(), "Nether Core");
		add(ModItems.MUSIC_DISK_ORCHELIAS_VOX.get(), "Hagali - Orchelia's vox (offvocal ver_)");
		add(ModMobEffects.COWARDICE.get(),"Cowardice");
		add(ModEnchantments.COUNTER_ATTACK.get(), "Counter Attack");
		add(ModEnchantments.BLOODTHIRSTY.get(), "Bloodthirsty");
		add(ModEnchantments.CLEAN_WATER.get(), "Clean Water");
		add(ModEnchantments.LIFE_TREE.get(), "Life Tree");
		add(ModEnchantments.DEVOURED.get(), "Devoured");
		add(ModEnchantments.FRANTIC.get(), "Frantic");
		add(ModEnchantments.EXECUTE.get(), "Execute");
		add(ModEnchantments.MOON.get(), "Moon");
		add(ModEnchantments.SUN.get(), "Sun");
		add(NetherRaid.NAME, "");
	}

}
