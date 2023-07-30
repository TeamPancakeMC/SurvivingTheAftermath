package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.capability.RaidData;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.RegisterCapabilitiesEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class ModCapability {
    public static final Capability<RaidData> RAID_DATA = CapabilityManager.get(new CapabilityToken<RaidData>() {
    });

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(RaidData.class);
    }
}