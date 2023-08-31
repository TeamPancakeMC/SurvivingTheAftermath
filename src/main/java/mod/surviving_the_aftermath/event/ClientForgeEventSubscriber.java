package mod.surviving_the_aftermath.event;

import mod.surviving_the_aftermath.Main;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.contents.TranslatableContents;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEventSubscriber {

	private static final ResourceLocation BARS = new ResourceLocation(Main.MODID, "textures/gui/bars.png");
	private static final int FRAME_WIDTH = 192;
	private static final int FRAME_HEIGHT = 22;
	private static final int FRAME_V = 5;
	private static final int BAR_WIDTH = 182;
	private static final int BAR_HEIGHT = 5;

	@SubscribeEvent
	public static void netherRaidProgress(CustomizeGuiOverlayEvent.BossEventProgress event) {
		ComponentContents contents = event.getBossEvent().getName().getContents();
		if (contents instanceof TranslatableContents translatableContents) {
			if (translatableContents.getKey().contains(Main.MODID + ".nether_raid")) {
				event.setCanceled(true);
				var graphics = event.getGuiGraphics();
				graphics.blit(BARS, (graphics.guiWidth() - FRAME_WIDTH) / 2, event.getY() - 9, 0, FRAME_V, FRAME_WIDTH,
						FRAME_HEIGHT);
				graphics.blit(BARS, (graphics.guiWidth() - BAR_WIDTH) / 2, event.getY() - 9 + 4, 0, 0,
						(int) (BAR_WIDTH * event.getBossEvent().getProgress()), BAR_HEIGHT);
				event.setIncrement(FRAME_HEIGHT);
			}
		}
	}
}
