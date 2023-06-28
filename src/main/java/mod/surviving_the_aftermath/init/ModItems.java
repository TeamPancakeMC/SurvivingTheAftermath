package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModItems {
	public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Main.MODID);

	static {
		ModBlocks.BLOCK_ITEMS.forEach(ITEMS::register);
	}

	public static final RegistryObject<Item> EXAMPLE = ITEMS.register("example",
			() -> new BlockItem(ModBlocks.EXAMPLE.get(), new Item.Properties()));


	private static RegistryObject<Item> register(String name, Supplier<Item> item) {
		return ITEMS.register(name, item);
	}

}
