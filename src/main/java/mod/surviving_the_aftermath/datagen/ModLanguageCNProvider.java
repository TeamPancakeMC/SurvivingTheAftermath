package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModBlocks;
import mod.surviving_the_aftermath.init.ModItems;
import mod.surviving_the_aftermath.raid.NetherRaid;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ModLanguageCNProvider extends LanguageProvider {

    public ModLanguageCNProvider(PackOutput output) {
        super(output, Main.MODID, "zh_cn");
    }

    @Override
    protected void addTranslations() {
        add("itemGroup." + Main.MODID, "幸存下来");
        add(ModBlocks.PARTY_BONFIRE.get(), "派对篝火");
        add(ModItems.RAW_FALUKORV.get(), "生的法伦香肠");
        add(ModItems.EGG_TART.get(), "蛋挞");
        add(ModItems.STACK_OF_EGG_TARTS.get(), "蛋挞堆");
        add(ModItems.HAMBURGER.get(), "汉堡");
        add(ModItems.TIANJIN_PANCAKE.get(), "煎饼果子");
        add("message.surviving_the_aftermath.nether_raid.start", "你感受到空气愈发炎热......");
        add("message.surviving_the_aftermath.nether_raid.victory", "望着最后一颗火星熄灭，你感觉它们不会再回来了，暂时......");
        add("message.surviving_the_aftermath.nether_raid.escape", "请勿战斗过程逃跑，否则你将付出代价，倒计时开始......%s");
        add("message.surviving_the_aftermath.nether_raid.personal_fail", "你虽然失败了但是可以继续相信你的队友");
        add("surviving_the_aftermath.nether_raid.wave", "地狱突袭 第%s波");
        add("surviving_the_aftermath.nether_raid.victory", "地狱突袭 胜利");
		add(NetherRaid.NAME, "");
    }

}