package com.pancake.surviving_the_aftermath.common.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class FranticEnchantment extends BaseEnchantment {

    public FranticEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND);
    }

    @Override
    protected boolean hasDamageBonus() {
        return true;
    }

    @Override
    protected float getDamageBonus(Player player, Entity target, int level) {
        return (float) ((1 - player.getHealth() / player.getMaxHealth()) * 0.1 * level * player.getAttributeValue(Attributes.ATTACK_DAMAGE));
    }
}