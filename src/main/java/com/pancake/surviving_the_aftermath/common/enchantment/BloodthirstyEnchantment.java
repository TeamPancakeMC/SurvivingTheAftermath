package com.pancake.surviving_the_aftermath.common.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class BloodthirstyEnchantment extends BaseEnchantment {

    public BloodthirstyEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

}