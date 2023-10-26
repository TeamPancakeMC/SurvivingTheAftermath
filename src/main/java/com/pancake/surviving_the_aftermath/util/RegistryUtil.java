package com.pancake.surviving_the_aftermath.util;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.ForgeRegistries;

public class RegistryUtil {
    public static EntityType<?> getEntityTypeFromRegistryName(String registryName) {
        return ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(registryName));
    }
    public static ResourceLocation getRegistryNameFromEntityType(EntityType<?> entityType) {
        return ForgeRegistries.ENTITY_TYPES.getKey(entityType);
    }


    public static Block getBlockFromRegistryName(String registryName) {
        return ForgeRegistries.BLOCKS.getValue(ResourceLocation.tryParse(registryName));
    }

    public static ResourceLocation getRegistryNameFromBlock(Block block) {
        return ForgeRegistries.BLOCKS.getKey(block);
    }

    public static Item getItemFromRegistryName(String registryName) {
        return ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(registryName));
    }

    public static ResourceLocation getRegistryNameFromItem(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }

}
