package mod.surviving_the_aftermath.event;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class NetherRaidEventSubscriber {
    @SubscribeEvent
    public static void onRaidStart(RaidEvent.Start event) {
        event.getPlayers().forEach(uuid -> {
            Player player = event.getLevel().getPlayerByUUID(uuid);
            if (player != null) {
                player.displayClientMessage(Component.translatable("message.surviving_the_aftermath.nether_raid.start"), true);
            }
        });
    }

    @SubscribeEvent
    public static void onRaidVictory(RaidEvent.Victory event) {
        event.getPlayers().forEach(uuid -> {
            Player player = event.getLevel().getPlayerByUUID(uuid);
            if (player != null) {
                player.displayClientMessage(Component.translatable("message.surviving_the_aftermath.nether_raid.victory"), true);
            }
        });
    }
}
