package mod.surviving_the_aftermath.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class RangerEnchantment extends BaseEnchantment {

    public RangerEnchantment() {
        super(Rarity.RARE, EnchantmentCategory.BOW, EquipmentSlot.MAINHAND);
    }

    @Override
    public int getMinCost(int level) {
        return 12 + (level - 1) * 20;
    }

    @Override
    public int getMaxCost(int level) {
        return getMinCost(level) + 50;
    }

}