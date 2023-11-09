package com.pancake.surviving_the_aftermath.common.event.subscriber;

import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.init.ModEnchantments;
import com.pancake.surviving_the_aftermath.common.init.ModItems;
import com.pancake.surviving_the_aftermath.common.init.ModVillagers;
import com.pancake.surviving_the_aftermath.common.util.RegistryUtil;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraftforge.event.village.VillagerTradesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.RegistryObject;

import java.util.List;

@Mod.EventBusSubscriber(modid = SurvivingTheAftermath.MOD_ID)
public class VillagerSubscriber {
    @SubscribeEvent
    public static void addCustomTrades(VillagerTradesEvent event) {
//        final ItemStack emerald = new ItemStack(Items.EMERALD, 2);
//        final ItemStack diamond = new ItemStack(Items.DIAMOND, 1);
//        event.getTrades();
//        if (event.getType() == ModVillagers.RELIC_DEALER.get()) {
//            trades1.get(1).add((trader, random) -> enchantBookForNetherCore(random));
//        }
//        if (event.getType() == VillagerProfession.BUTCHER) {
//            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = trades1;
//            ItemStack rawFalukorv = new ItemStack(ModItems.RAW_FALUKORV.get(), 1);
//            trades.get(4).add((trader, random) -> newOffer(emerald, rawFalukorv));
//            trades.get(5).add((trader, random) -> newOffer(diamond, rawFalukorv));
//        }
//        if (event.getType() == VillagerProfession.FARMER) {
//            Int2ObjectMap<List<VillagerTrades.ItemListing>> trades = trades1;
//            for (Item item : RegistryUtil.getKnownItems()) {
//                if (item.isEdible()) {
//                    ItemStack foodStack = item.getDefaultInstance();
//                    trades.get(4).add((trader, random) -> newOffer(emerald, foodStack));
//                    trades.get(5).add((trader, random) -> newOffer(diamond, foodStack));
//                }
//            }
//        }
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
}
