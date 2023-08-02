package mod.surviving_the_aftermath.enchantment;

import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentCategory;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.ToolActions;

public class BaseEnchantment extends Enchantment {

    public BaseEnchantment(Rarity rarity, EnchantmentCategory category, EquipmentSlot... applicableSlots) {
        super(rarity, category, applicableSlots);
    }

    @Override
    public boolean isTreasureOnly() {
        return true;
    }

    @Override
    public boolean isTradeable() {
        return true;
    }

    protected boolean hasDamageBonus() {
        return false;
    }

    protected float getDamageBonus(Player player, Entity target, int level) {
        return 0.0F;
    }

    @Override
    public void doPostAttack(LivingEntity attacker, Entity target, int level) {
        if (this.hasDamageBonus() && attacker instanceof Player player && target.isAttackable() && !target.skipAttackInteraction(attacker)) {
            MobType mobType = target instanceof LivingEntity livingEntity ? livingEntity.getMobType() : MobType.UNDEFINED;
            float mobTypeDamageBonus = EnchantmentHelper.getDamageBonus(player.getMainHandItem(), mobType);
            float customDamageBonus = this.getDamageBonus(player, target, level);
            float f = (float)attacker.getAttributeValue(Attributes.ATTACK_DAMAGE);
            float f1 = customDamageBonus > 0.0F ? customDamageBonus : mobTypeDamageBonus;
            float f2 = player.getAttackStrengthScale(0.5F);
            if ((f *= 0.2F + f2 * f2 * 0.8F) > 0.0F || (f1 *= f2) > 0.0F) {
                boolean flag = f2 > 0.9F, flag3 = false;
                boolean flag2 = flag && player.fallDistance > 0.0F && !player.onGround() && !player.onClimbable() && !player.isInWater() &&
                        !player.hasEffect(MobEffects.BLINDNESS) && !player.isPassenger() && target instanceof LivingEntity;
                flag2 = flag2 && !player.isSprinting();
                f *= flag2 ? 1.5F : 1.0F; f += f1;
                double d0 = player.walkDist - player.walkDistO;
                if (flag && !flag2 && !player.isSprinting() && player.onGround() && d0 < (double) player.getSpeed()) {
                    ItemStack itemInHand = player.getItemInHand(InteractionHand.MAIN_HAND);
                    flag3 = itemInHand.canPerformAction(ToolActions.SWORD_SWEEP);
                }
                if (target.hurt(player.damageSources().playerAttack(player), f)) {
                    player.heal(this instanceof BloodthirstyEnchantment ? f * 0.05F * level : 0.0F);
                    if (flag3) {
                        float f3 = 1.0F + EnchantmentHelper.getSweepingDamageRatio(player) * f;
                        AABB sweepHitBox = player.getItemInHand(InteractionHand.MAIN_HAND).getSweepHitBox(player, target);
                        for (LivingEntity livingEntity : player.level().getEntitiesOfClass(LivingEntity.class, sweepHitBox)) {
                            double entityReachSq = Mth.square(player.getEntityReach());
                            double x = Mth.sin(player.getYRot() * ((float)Math.PI / 180F));
                            double z = -Mth.cos(player.getYRot() * ((float)Math.PI / 180F));
                            boolean flag4 = player.distanceToSqr(livingEntity) < entityReachSq;
                            boolean flag5 = (!(livingEntity instanceof ArmorStand) || !((ArmorStand)livingEntity).isMarker());
                            if (livingEntity != player && livingEntity != target && !player.isAlliedTo(livingEntity) && flag4 && flag5) {
                                if (this instanceof BloodthirstyEnchantment) {
                                    player.heal(f3 * 0.05F * level);
                                } else {
                                    livingEntity.knockback(0.4F, x, z);
                                    livingEntity.hurt(player.damageSources().playerAttack(player), f3);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

}