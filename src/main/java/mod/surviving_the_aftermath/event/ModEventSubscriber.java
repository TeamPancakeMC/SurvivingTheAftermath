package mod.surviving_the_aftermath.event;

import com.google.common.collect.Lists;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModEnchantments;
import mod.surviving_the_aftermath.init.ModItems;
import mod.surviving_the_aftermath.init.ModVillagers;
import mod.surviving_the_aftermath.util.ModCommonUtils;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EquipmentSlot;
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
import net.minecraftforge.event.ItemAttributeModifierEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import static mod.surviving_the_aftermath.datagen.ModItemModelProvider.ENCHANTMENTS;

@EventBusSubscriber(modid = Main.MODID)
public class ModEventSubscriber {

    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
        final ItemStack emerald = new ItemStack(Items.EMERALD, 2);
        final ItemStack diamond = new ItemStack(Items.DIAMOND, 1);
        if (event.getType() == ModVillagers.RELIC_DEALER.get()) {
            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = event.getTrades();
            trades.get(1).add((trader, random) -> enchantBookForNetherCore(random));
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

    private static MerchantOffer enchantBookForNetherCore(RandomSource random) {
        List<RegistryObject<Enchantment>> list = Lists.newArrayList(ModEnchantments.ENCHANTMENTS.getEntries());
        Enchantment enchantment = list.get(random.nextInt(list.size())).get();
        int i = Mth.nextInt(random, enchantment.getMinLevel(), enchantment.getMaxLevel());
        int j = 2 + random.nextInt(5 + i * 10) + 3 * i;
        ItemStack netherCore = new ItemStack(ModItems.NETHER_CORE.get(), Math.min(j, 64));
        ItemStack enchantedBook = EnchantedBookItem.createForEnchantment(new EnchantmentInstance(enchantment, i));
        return new MerchantOffer(netherCore, new ItemStack(Items.BOOK), enchantedBook, 12, 30, 0.2F);
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
                    CompoundTag tag = itemInHand.getOrCreateTag();
                    float addition = tag.contains("surviving_the_aftermath.devoured") ? tag.getFloat("surviving_the_aftermath.devoured") : 0;
                    if (addition < enchantmentLevel + 1) {
                        tag.putFloat("surviving_the_aftermath.devoured", addition + 0.1F);
                        player.setItemInHand(InteractionHand.MAIN_HAND, itemInHand); // Sync
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
            CompoundTag tag = event.getItemStack().getOrCreateTag();
            if (tag.contains("surviving_the_aftermath.devoured")) {
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
        ItemProperties.register(Items.ENCHANTED_BOOK, Main.asResource("special"), (stack, world, entity, i) -> {
            Map<Enchantment, Integer> map = EnchantmentHelper.getEnchantments(stack);
            if (map.size() != 1) return 0.0F;
            String key = map.entrySet().iterator().next().getKey().getDescriptionId();
            return ENCHANTMENTS.getOrDefault(key, 0.0F);
        });
    }

}