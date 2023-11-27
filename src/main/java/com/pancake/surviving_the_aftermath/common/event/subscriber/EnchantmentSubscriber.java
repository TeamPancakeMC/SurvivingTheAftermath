package com.pancake.surviving_the_aftermath.common.event.subscriber;

import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.data.datagen.ModItemModelProvider;
import com.pancake.surviving_the_aftermath.common.init.ModEnchantments;
import com.pancake.surviving_the_aftermath.common.init.ModItems;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

@Mod.EventBusSubscriber
public class EnchantmentSubscriber {

    @SubscribeEvent
    public static void onLivingUseItemTicking(LivingEntityUseItemEvent.Tick event) {
        int rangerLevel = event.getItem().getEnchantmentLevel(ModEnchantments.RANGER.get());
        if (rangerLevel > 0 && event.getDuration() > event.getItem().getUseDuration() - 20) {
            event.setDuration(event.getDuration() - rangerLevel);
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            int enchantmentLevel = itemInHand.getEnchantmentLevel(ModEnchantments.BLOODTHIRSTY.get());
            if (!player.level().isClientSide && !itemInHand.isEmpty() && enchantmentLevel > 0) {
                player.heal(event.getAmount() * 0.05F * enchantmentLevel);
            }
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            if (!player.level().isClientSide && (itemInHand.getItem() instanceof TieredItem || itemInHand.getItem() instanceof TridentItem)) {
                int enchantmentLevel = itemInHand.getEnchantmentLevel(ModEnchantments.DEVOURED.get());
                if (enchantmentLevel > 0 && player.getRandom().nextInt(10) < enchantmentLevel) {
                    CompoundTag tag = itemInHand.getTag();
                    if (tag != null){
                        float addition = tag.contains("surviving_the_aftermath.devoured") ? tag.getFloat("surviving_the_aftermath.devoured") : 0;
                        if (addition < enchantmentLevel + 1) {
                            tag.putFloat("surviving_the_aftermath.devoured", addition + 0.1F);
                            player.setItemInHand(InteractionHand.MAIN_HAND, itemInHand); // Sync
                        }
                    }
                }
            }
        }
    }

    private final static Function<Float, AttributeModifier> DEVOURED_ATTRIBUTE = (addition) ->
            new AttributeModifier(UUID.fromString("412C831F-22EA-43B8-B74B-D172019AD3D2"),
                    "devoured_enchantment", addition, AttributeModifier.Operation.ADDITION);

    private final static Function<Integer,AttributeModifier> LIFE_TREE_ATTRIBUTE = (addition) ->
            new AttributeModifier(UUID.fromString("312C831F-22EA-43B8-B74B-D172019AD3D2"),
                    "life_tree_enchantment", addition * 0.1, AttributeModifier.Operation.MULTIPLY_BASE);

    @SubscribeEvent
    public static void onAttributeGet(ItemAttributeModifierEvent event) {
        if (event.getSlotType() == EquipmentSlot.MAINHAND) {
            CompoundTag tag = event.getItemStack().getTag();
            if (tag != null && tag.contains("surviving_the_aftermath.devoured")) {
                event.addModifier(Attributes.ATTACK_DAMAGE, DEVOURED_ATTRIBUTE.apply(tag.getFloat("surviving_the_aftermath.devoured")));
            }
        }
        if(event.getItemStack().getItem() instanceof ArmorItem armorItem && armorItem.getEquipmentSlot() == event.getSlotType()){
            int lifeTreeLevel = event.getItemStack().getEnchantmentLevel(ModEnchantments.LIFE_TREE.get());
            if (lifeTreeLevel != 0) {
                event.addModifier(Attributes.MAX_HEALTH, LIFE_TREE_ATTRIBUTE.apply(lifeTreeLevel));
            }
        }
    }

    @SuppressWarnings("unused")
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        ItemProperties.register(Items.ENCHANTED_BOOK, SurvivingTheAftermath.asResource("special"), (stack, world, entity, i) -> {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
            if (map.size() != 1) return 0.0F;
            String key = map.entrySet().iterator().next().getKey().getDescriptionId();
            return ModItemModelProvider.ENCHANTMENTS.getOrDefault(key, 0.0F);
        });
    }
}
