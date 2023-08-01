package mod.surviving_the_aftermath.util;

import mod.surviving_the_aftermath.init.ModBlocks;
import mod.surviving_the_aftermath.init.ModEnchantments;
import mod.surviving_the_aftermath.init.ModItems;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public class ModCommonUtils {

    public static final Supplier<Enchantment>[] ENCHANTMENTS = new Supplier[]{null,
            ModEnchantments.COUNTER_ATTACK, ModEnchantments.BLOODTHIRSTY, ModEnchantments.CLEAN_WATER,
            ModEnchantments.LIFE_TREE, ModEnchantments.DEVOURED, ModEnchantments.FRANTIC,
            ModEnchantments.EXECUTE, ModEnchantments.MOON, ModEnchantments.SUN};

    public static Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    public static Iterable<Item> getKnownItems() {
        return ModItems.ITEMS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    public static Iterable<Enchantment> getKnownEnchantments() {
        return ModEnchantments.ENCHANTMENTS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

}