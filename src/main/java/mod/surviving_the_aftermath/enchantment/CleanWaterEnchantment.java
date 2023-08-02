package mod.surviving_the_aftermath.enchantment;

import mod.surviving_the_aftermath.init.ModTags;
import net.minecraft.tags.EntityTypeTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.DamageEnchantment;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class CleanWaterEnchantment extends BaseEnchantment {

    public CleanWaterEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getMaxLevel() {
        return 4;
    }

    @Override
    protected boolean checkCompatibility(Enchantment other) {
        return !(other instanceof DamageEnchantment) && super.checkCompatibility(other);
    }

    @Override
    protected boolean hasDamageBonus() {
        return true;
    }

    @Override
    protected float getDamageBonus(Player player, Entity target, int level) {
        return target.getType().is(ModTags.NETHER_MOB) ? level * 2.5F : 0.0F;
    }

}