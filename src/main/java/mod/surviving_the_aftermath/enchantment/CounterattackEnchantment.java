package mod.surviving_the_aftermath.enchantment;

import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.Enchantments;

public class CounterattackEnchantment extends BaseEnchantment {

    public CounterattackEnchantment() {
        super(Enchantment.Rarity.RARE, EnchantmentCategory.ARMOR, Enchantments.ARMOR_SLOTS);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public int getMinCost(int level) {
        return 10 + level * 7;
    }

    @Override
    public int getMaxCost(int level) {
        return 50;
    }

    @Override
    public void doPostHurt(LivingEntity target, Entity entity, int level){
        if (target instanceof Player player && entity.isAttackable()) {
            float f = player.getAttackStrengthScale(0.5F);
            float i = (float)player.getAttributeValue(Attributes.ATTACK_KNOCKBACK) + level;
            if ((i += player.isSprinting() && f > 0.9F ? 1.0F : 0.0F) > 0) {
                if (entity instanceof LivingEntity livingEntity) {
                    double x = Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
                    double z = -Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
                    livingEntity.knockback(i * 0.5F, x, z);
                } else {
                    double x = -Mth.sin(player.getYRot() * ((float)Math.PI / 180F)) * i * 0.5F;
                    double z = Mth.cos(player.getYRot() * ((float)Math.PI / 180F)) * i * 0.5F;
                    entity.push(x, 0.1D, z);
                }

                player.setDeltaMovement(player.getDeltaMovement().multiply(0.6D, 1.0D, 0.6D));
                player.setSprinting(false);
            }
        }
    }

}