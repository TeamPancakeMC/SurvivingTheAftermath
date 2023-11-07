package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModTabs {

	public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister
			.create(Registries.CREATIVE_MODE_TAB.location(), SurvivingTheAftermath.MOD_ID);

	public static final RegistryObject<CreativeModeTab> TAB = TABS.register("tab",
			() -> CreativeModeTab.builder().title(Component.translatable("itemGroup." + SurvivingTheAftermath.MOD_ID))
					.icon(() -> new ItemStack(ModItems.NETHER_CORE.get()))
					.displayItems((params, output) -> ModItems.ITEMS.getEntries()
							.forEach(item -> output.accept(item.get()))).build());

}