package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModBlocks;
import mod.surviving_the_aftermath.init.ModItems;
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
		add(ModBlocks.EXAMPLE.get(), "Example");
		add(ModBlocks.PARTY_BONFIRE.get(), "Party Bonfire");
		add(ModItems.RAW_FALUKORV.get(), "Raw Falukorv");
		add(ModItems.EGG_TART.get(), "Egg Tart");
		add(ModItems.STACK_OF_EGG_TARTS.get(), "Stack of Egg Tart");
		add(ModItems.HAMBURGER.get(), "Hamburger");
		add(ModItems.TIANJIN_PANCAKE.get(), "Tianjin Pancake");
		add(NetherRaid.NAME, "");
	}

}
