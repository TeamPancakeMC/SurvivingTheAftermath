package mod.surviving_the_aftermath.enchantment;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;

public class ExecuteEnchantment extends BaseEnchantment {

    public ExecuteEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.WEAPON, new EquipmentSlot[]{EquipmentSlot.MAINHAND});
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return 20;
    }

    @Override
    public int getMaxCost(int level) {
        return 50;
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
        float[] percentage = new float[]{0.0F, 0.01F, 0.03F, 0.05F};
        if (target instanceof LivingEntity livingEntity) {
            float maxHealth = livingEntity.getMaxHealth();
            float currentHealth = livingEntity.getHealth();
            if (currentHealth / maxHealth < percentage[level]) {
                target.kill();
            }
        }
    }

}