package mod.surviving_the_aftermath.event;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModEnchantments;
import mod.surviving_the_aftermath.init.ModItems;
import mod.surviving_the_aftermath.init.ModVillagers;
import mod.surviving_the_aftermath.util.ModCommonUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.function.Supplier;

@EventBusSubscriber(modid = Main.MODID)
public class ModEventSubscriber {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        final ItemStack emerald = new ItemStack(Items.EMERALD, 2);
        final ItemStack diamond = new ItemStack(Items.DIAMOND, 1);
        if (event.getType() == ModVillagers.RELIC_DEALER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((trader, random) -> EnchantBookForNetherCore(random));
        }
        if (event.getType() == VillagerProfession.BUTCHER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            ItemStack rawFalukorv = new ItemStack(ModItems.RAW_FALUKORV.get(), 1);
            trades.get(4).add((trader, random) -> newOffer(emerald, rawFalukorv));
            trades.get(5).add((trader, random) -> newOffer(diamond, rawFalukorv));
        }
        if (event.getType() == VillagerProfession.FARMER) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            for (Item item : ModCommonUtils.getKnownItems()) {
                if (item.isEdible()) {
                    ItemStack foodStack = item.getDefaultInstance();
                    trades.get(4).add((trader, random) -> newOffer(emerald, foodStack));
                    trades.get(5).add((trader, random) -> newOffer(diamond, foodStack));
                }
            }
        }
    }

    private static MerchantOffer newOffer(ItemStack baseCostA, ItemStack result) {
        return new MerchantOffer(baseCostA, result, 12, 30, 1.0F);
    }

    private static MerchantOffer EnchantBookForNetherCore(RandomSource random) {
        List<RegistryObject<Enchantment>> list = ModEnchantments.ENCHANTMENTS.getEntries().stream().toList();
        Enchantment enchantment = list.get(random.nextInt(list.size())).get();
        int i = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
        ItemStack netherCore = new ItemStack(ModItems.NETHER_CORE.get(), Math.min(j, 64));
        ItemStack enchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));
        return new MerchantOffer(netherCore, new ItemStack(Items.BOOK), enchantedBook, 12, 30, 0.2F);
    }

    @SubscribeEvent
    public static void onPlayerTicking(TickEvent.PlayerTickEvent event) {
        int totalLifeTreeLevel = 0;
        Player player = event.player;
        for (EquipmentSlot slots : EquipmentSlot.values()) {
            Item slotItems = player.getItemBySlot(slots).getItem();
            ItemStack slotStacks = slotItems.getDefaultInstance();
            int lifeTreeLevel = slotStacks.getEnchantmentLevel(ModEnchantments.LIFE_TREE.get());
            boolean flag = slots.getType() == EquipmentSlot.Type.ARMOR && slotItems instanceof ArmorItem;
            totalLifeTreeLevel += flag && lifeTreeLevel > 0 ? lifeTreeLevel : 0;
        }
        if (totalLifeTreeLevel > 0) {
            MobEffectInstance healthBoost = new MobEffectInstance(MobEffects.HEALTH_BOOST, 100);
            healthBoost.amplifier = totalLifeTreeLevel;
            healthBoost.ambient = false;
            healthBoost.visible = false;
            healthBoost.showIcon = false;
            player.addEffect(healthBoost);
        }
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (event.getSource().getEntity() instanceof Player player) {
            ItemStack itemInHand = player.getItemInHand(player.getUsedItemHand());
            if (!player.level().isClientSide && !itemInHand.isEmpty()) {
                int enchantmentLevel = itemInHand.getEnchantmentLevel(ModEnchantments.DEVOURED.get());
                double attackDamage = player.getAttributeBaseValue(Attributes.ATTACK_DAMAGE);
                if (enchantmentLevel > 0 && player.getRandom().nextInt(10) < enchantmentLevel) {
                    if (itemInHand.getItem() instanceof SwordItem swordItem) {
                        Multimap<Attribute, AttributeModifier> multimap = swordItem.defaultModifiers;
                        Multimap<Attribute, AttributeModifier> multimap1 = ArrayListMultimap.create(multimap);
                        swordItem.defaultModifiers = ImmutableMultimap.copyOf(attributeModifierBuilder(attackDamage, multimap1, "Weapon modifier"));
                    } else if (itemInHand.getItem() instanceof DiggerItem diggerItem) {
                        Multimap<Attribute, AttributeModifier> multimap = diggerItem.defaultModifiers;
                        Multimap<Attribute, AttributeModifier> multimap1 = ArrayListMultimap.create(multimap);
                        diggerItem.defaultModifiers = ImmutableMultimap.copyOf(attributeModifierBuilder(attackDamage, multimap1, "Tool modifier"));
                    }
                }
            }
        }
    }

    private static Multimap<Attribute, AttributeModifier> attributeModifierBuilder(double amount, Multimap<Attribute, AttributeModifier> multimap, String name) {
        AttributeModifier.Operation operation = AttributeModifier.Operation.ADDITION;
        ImmutableMultimap.Builder<Attribute, AttributeModifier> builder = ImmutableMultimap.builder();
        for (AttributeModifier modifier1 : multimap.values()) {
            amount += modifier1.getAmount();
        }
        amount += 0.05D;
        AttributeModifier modifier = new AttributeModifier(Item.BASE_ATTACK_DAMAGE_UUID, name, amount, operation);
        multimap.removeAll(Attributes.ATTACK_DAMAGE);
        builder.put(Attributes.ATTACK_DAMAGE, modifier);
        return builder.build();
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    @SuppressWarnings("unused")
    public static void onFMLClientSetup(FMLClientSetupEvent event) {
        ResourceLocation name = Main.asResource("special");
        Supplier<Enchantment>[] enchantments = ModCommonUtils.ENCHANTMENTS;
        ItemProperties.register(Items.ENCHANTED_BOOK, name, (stack, level, entity, seed) -> {
            for (int i = 1; i < enchantments.length; i++) {
                int enchantmentCount = EnchantmentHelper.getEnchantments(stack).keySet().size();
                if (stack.getEnchantmentLevel(enchantments[i].get()) > 0) {
                    return i / 10.0F;
                }
            }
            return 0.0F;
        });
    }

}