package com.pancake.surviving_the_aftermath.common.util;

import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.common.init.ModBlocks;
import com.pancake.surviving_the_aftermath.common.init.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;

public class RegistryUtil {
    public static final Logger LOGGER = LogUtils.getLogger();
    public static EntityType<?> getEntityTypeFromRegistryName(String registryName) {
        EntityType<?> entityType = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(registryName));
        if (entityType == null) {
            LOGGER.error("Entity with registry name {} does not exist!", registryName);
        }
        return entityType;
    }
    public static ResourceLocation getRegistryNameFromEntityType(EntityType<?> entityType) {
        return ForgeRegistries.ENTITY_TYPES.getKey(entityType);
    }


    public static Block getBlockFromRegistryName(String registryName) {
        Block block = ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(registryName));
        if (block == null) {
            LOGGER.error("Block with registry name {} does not exist!", registryName);
        }
        return block;
    }

    public static ResourceLocation getRegistryNameFromBlock(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static Item getItemFromRegistryName(String registryName) {
        Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(registryName));
        if (item == null) {
            LOGGER.error("Item with registry name {} does not exist!", registryName);
        }
        return item;
    }

    public static ResourceLocation getRegistryNameFromItem(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }


    public static Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    public static Iterable<Item> getKnownItems() {
        return ModItems.ITEMS.getEntries().stream().map(RegistryObject::get)::iterator;
    }

}
