package mod.surviving_the_aftermath;

import mod.surviving_the_aftermath.init.*;
import net.minecraft.resources.ResourceLocation;
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
		ModSoundEvents.SOUND_EVENTS.register(bus);
	}

	public static ResourceLocation asResource(String name) {
		return new ResourceLocation(MODID, name);
	}

}
