package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModBlocks;
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
        add("message.surviving_the_aftermath.raid_completed", "你听到了村民的欢呼声，今晚注定无眠");
	add(NetherRaid.NAME, "");
	add(NetherRaid.NAME + ".begin", "你感受到空气愈发炎热……");
	add(NetherRaid.NAME + ".end", "望着最后一颗火星熄灭，你感觉它们不会再回来了，暂时……");
    }
}
