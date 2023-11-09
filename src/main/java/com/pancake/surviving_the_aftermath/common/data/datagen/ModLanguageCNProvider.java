package com.pancake.surviving_the_aftermath.common.data.datagen;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.event.subscriber.RaidEventSubscriber;
import com.pancake.surviving_the_aftermath.common.init.ModEnchantments;
import com.pancake.surviving_the_aftermath.common.init.ModItems;
import com.pancake.surviving_the_aftermath.common.init.ModMobEffects;
import com.pancake.surviving_the_aftermath.common.tracker.RaidPlayerBattleTracker;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageCNProvider extends LanguageProvider {

    public ModLanguageCNProvider(PackOutput output) {
        super(output, SurvivingTheAftermath.MOD_ID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + SurvivingTheAftermath.MOD_ID, "劫后余生");
        add(ModItems.RAW_FALUKORV.get(), "生的法伦香肠");
        add(ModItems.COOKED_FALUKORV.get(), "熟的法伦香肠");
        add(ModItems.EGG_TART.get(), "蛋挞");
        add(ModItems.STACK_OF_EGG_TARTS.get(), "蛋挞堆");
        add(ModItems.HAMBURGER.get(), "汉堡");
        add(ModItems.TIANJIN_PANCAKE.get(), "煎饼果子");
        add(ModItems.NETHER_CORE.get(), "下界核心");
        add(ModItems.MUSIC_DISK_ORCHELIAS_VOX.get(), "音乐唱片");
        add(ModMobEffects.COWARDICE.get(),"懦弱");
        add(ModEnchantments.COUNTER_ATTACK.get(), "反击");
        add(ModEnchantments.BLOODTHIRSTY.get(), "渴血");
        add(ModEnchantments.CLEAN_WATER.get(), "净水");
        add(ModEnchantments.LIFE_TREE.get(), "树灵");
        add(ModEnchantments.DEVOURED.get(), "吞噬");
        add(ModEnchantments.FRANTIC.get(), "癫狂");
        add(ModEnchantments.EXECUTE.get(), "处决");
        add(ModEnchantments.RANGER.get(), "游侠");
        add(ModEnchantments.MOON.get(), "皎月");
        add(ModEnchantments.SUN.get(), "烈阳");
        add(RaidEventSubscriber.NETHER_RAID_START, "你感受到空气愈发炎热......");
        add(RaidEventSubscriber.NETHER_RAID_VICTORY, "望着最后一颗火星熄灭，你感觉它们不会再回来了，暂时......");
        add(RaidPlayerBattleTracker.PLAYER_BATTLE_ESCAPE, "请勿战斗过程逃跑，否则你将付出代价，倒计时开始......%s");
        add(RaidPlayerBattleTracker.PLAYER_BATTLE_PERSONAL_FAIL, "你虽然失败了但是可以继续相信你的队友");
//        add(Main.MODID + NetherRaid.IDENTIFIER + ".wave", "地狱突袭 第%s波");
//        add(Main.MODID + NetherRaid.IDENTIFIER + ".victory", "地狱突袭 胜利");
    }

}