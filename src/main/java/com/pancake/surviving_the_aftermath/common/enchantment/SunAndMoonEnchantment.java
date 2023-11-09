package com.pancake.surviving_the_aftermath.common.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class SunAndMoonEnchantment extends BaseEnchantment {

    public final int type;

    public SunAndMoonEnchantment(int type) {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND);
        this.type = type;
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof SunAndMoonEnchantment);
    }

    @Override
    protected boolean hasDamageBonus() {
        return true;
    }

    @Override
    protected float getDamageBonus(Player player, Entity target, int level) {
        return (this.type == 0 && player.level().isDay()) || (this.type == 1 && player.level().isNight()) ? level : 0.0F;
    }

}