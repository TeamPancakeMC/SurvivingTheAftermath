package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.block.ExampleBlock;
import mod.surviving_the_aftermath.block.PartyBonfireBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Main.MODID);
	public static final Map<String, Supplier<BlockItem>> BLOCK_ITEMS = new LinkedHashMap<>();


	public static final RegistryObject<Block> EXAMPLE = BLOCKS.register("example", () -> new ExampleBlock(
			BlockBehaviour.Properties.of().strength(2, 3).sound(SoundType.WOOD).mapColor(MapColor.WOOD)));

	public static final RegistryObject<Block> PARTY_BONFIRE = register("party_bonfire", PartyBonfireBlock::new);

	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, supplier -> () -> new BlockItem(supplier.get(),new Item.Properties()));
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> block,
												  Function<RegistryObject<Block>, Supplier<? extends BlockItem>> item) {
		RegistryObject<Block> register = BLOCKS.register(name, block);
		BLOCK_ITEMS.put(name, () -> item.apply(register).get());
		return register;
	}
}
