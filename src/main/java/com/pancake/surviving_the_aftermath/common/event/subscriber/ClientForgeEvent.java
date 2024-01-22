package com.pancake.surviving_the_aftermath.common.event.subscriber;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.AftermathManager;
import net.minecraft.client.gui.components.LerpingBossEvent;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.CustomizeGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;


@Mod.EventBusSubscriber(modid = SurvivingTheAftermath.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientForgeEvent {
    @SubscribeEvent
    public static void netherRaidProgress(CustomizeGuiOverlayEvent.BossEventProgress event) {
        LerpingBossEvent bossEvent = event.getBossEvent();
        AftermathManager manager = AftermathManager.getInstance();
        manager.getAftermath(bossEvent.getId()).ifPresent(aftermath -> {
            var graphics = event.getGuiGraphics();
            ResourceLocation resource = aftermath.getBarsResource();
            int[] offset = aftermath.getBarsOffset();

            if (resource == null || offset == null) {
                return;
            }

            int frameWidth = offset[0];
            int frameHeight = offset[1];
            int barWidth = offset[2];
            int barHeight = offset[3];
            int frameOffset = offset[4];
            int barOffset = offset[5];

            //渲染进度条框
            graphics.blit(resource, (graphics.guiWidth() - frameWidth) / 2, event.getY() - 10,
                    0, frameOffset, frameWidth, frameHeight);
            //渲染进度条
            graphics.blit(resource, (graphics.guiWidth() - barWidth) / 2, event.getY() - 10 + barOffset,
                    0, 0, (int) (barWidth * event.getBossEvent().getProgress()), barHeight);
            event.setIncrement(frameHeight);
            event.setCanceled(true);
        });
    }
}
