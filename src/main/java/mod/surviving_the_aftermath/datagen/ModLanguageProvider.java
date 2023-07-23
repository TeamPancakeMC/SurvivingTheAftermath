package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModBlocks;
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
		add(NetherRaid.NAME, "");
		add(NetherRaid.NAME + ".begin": "");
		add(NetherRaid.NAME + ".end", "");
	}
}
