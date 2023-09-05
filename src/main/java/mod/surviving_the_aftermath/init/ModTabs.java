package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(bus = Bus.MOD, modid = Main.MODID)
public class ModTabs {

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB.location(), Main.MODID);

	public static final RegistryObject<CreativeModeTab> TAB = TABS.register("tab",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + Main.MODID))
					.icon(() -> new ItemStack(ModItems.NETHER_CORE.get()))
					.displayItems((params, output) -> ModItems.ITEMS.getEntries()
							.forEach(item -> output.accept(item.get()))).build());

}