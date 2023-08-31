package mod.surviving_the_aftermath;

import mod.surviving_the_aftermath.data.Easy;
import mod.surviving_the_aftermath.data.Hard;
import mod.surviving_the_aftermath.data.Normal;
import mod.surviving_the_aftermath.data.Peaceful;
import mod.surviving_the_aftermath.datagen.EventSubscriber;
import mod.surviving_the_aftermath.event.ModEventSubscriber;
import mod.surviving_the_aftermath.init.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Main.MODID)
public class Main {

	public static final String MODID = "surviving_the_aftermath";

	public Main() {
		IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
		ModBlocks.BLOCKS.register(bus);
		ModItems.ITEMS.register(bus);
		ModTabs.TABS.register(bus);
		ModVillagers.VILLAGER_PROFESSIONS.register(bus);
		ModBlockEntities.BLOCK_ENTITIES.register(bus);
		ModEnchantments.ENCHANTMENTS.register(bus);
		ModSoundEvents.SOUND_EVENTS.register(bus);
		ModStructurePieceTypes.STRUCTURE_PIECE_TYPES.register(bus);
		ModStructureTypes.STRUCTURE_TYPES.register(bus);
		ModMobEffects.MOB_EFFECTS.register(bus);
		bus.addListener(EventSubscriber::onGatherData);
		bus.addListener(ModEventSubscriber::onFMLClientSetup);
		MinecraftForge.EVENT_BUS.register(this);
		MinecraftForge.EVENT_BUS.addListener(this::onDataPackLoad);
	}

	public static ResourceLocation asResource(String name) {
		return new ResourceLocation(MODID, name);
	}


	private void onDataPackLoad(AddReloadListenerEvent event) {
		event.addListener(Peaceful.getInstance());
		event.addListener(Easy.getInstance());
		event.addListener(Normal.getInstance());
		event.addListener(Hard.getInstance());
	}
}