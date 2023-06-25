package mod.surviving_the_aftermath.event;

import java.util.HashSet;
import java.util.Set;

import mod.surviving_the_aftermath.Main;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;

@EventBusSubscriber(modid = Main.MODID, bus = Bus.FORGE)
public class ForgeEventSubscriber {

	private static final Set<Integer> RAIDS = new HashSet<>();

	@SubscribeEvent
	public static void raidCompleted(TickEvent.PlayerTickEvent event) {
		var player = event.player;
		if (player.level() instanceof ServerLevel level) {
			var raid = level.getRaidAt(player.blockPosition());

			if (raid != null && raid.isVictory() && !RAIDS.contains(raid.getId())) {
				var villager = EntityType.VILLAGER.create(level);
				villager.moveTo(player.position());
				level.addFreshEntity(villager);
				RAIDS.add(raid.getId());
			}
		}
	}
}
