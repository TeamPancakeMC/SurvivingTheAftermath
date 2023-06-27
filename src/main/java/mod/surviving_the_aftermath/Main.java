package mod.surviving_the_aftermath;

import mod.surviving_the_aftermath.init.ModBlockEntities;
import mod.surviving_the_aftermath.init.ModBlocks;
import mod.surviving_the_aftermath.init.ModItems;
import mod.surviving_the_aftermath.init.ModTabs;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main {
	public static final String MODID = "surviving_the_aftermath";

	public Main() {
		var bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.BLOCKS.register(bus);
		ModItems.ITEMS.register(bus);
		ModTabs.TABS.register(bus);
		ModBlockEntities.BLOCK_ENTITIES.register(bus);
	}
}
