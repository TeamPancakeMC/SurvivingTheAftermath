package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Supplier;

public class ModBlocks {

	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, SurvivingTheAftermath.MOD_ID);
	public static final Map<String, Supplier<BlockItem>> BLOCK_ITEMS = new LinkedHashMap<>();

	private static RegistryObject<Block> register(String name, Supplier<Block> block) {
		return register(name, block, supplier -> () -> new BlockItem(supplier.get(),new Item.Properties()));
	}

	private static RegistryObject<Block> register(String name, Supplier<Block> block, Function<RegistryObject<Block>, Supplier<? extends BlockItem>> item) {
		RegistryObject<Block> register = BLOCKS.register(name, block);
		BLOCK_ITEMS.put(name, () -> item.apply(register).get());
		return register;
	}

}