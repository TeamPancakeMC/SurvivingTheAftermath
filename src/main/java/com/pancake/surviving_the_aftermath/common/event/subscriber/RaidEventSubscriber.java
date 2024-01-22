package com.pancake.surviving_the_aftermath.common.event.subscriber;


import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.common.init.ModSoundEvents;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.MagmaCube;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;



@Mod.EventBusSubscriber
public class RaidEventSubscriber {
    public static final String NETHER_RAID_START = "message.surviving_the_aftermath.nether_raid.start";
    public static final String NETHER_RAID_VICTORY = "message.surviving_the_aftermath.nether_raid.victory";
    @SubscribeEvent
    public static void netherRaid(EntityTravelToDimensionEvent event) {
        Entity entity = event.getEntity();
        Level level = entity.level();
        BlockPos pos = entity.blockPosition();
        if (level instanceof ServerLevel serverLevel) {
            event.setCanceled(serverLevel.structureManager().getAllStructuresAt(pos)
                    .containsKey(level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID)));
        }
    }

    @SubscribeEvent
    public static void onBlock(BlockEvent.PortalSpawnEvent event) {
        LevelAccessor level = event.getLevel();
        if (level instanceof ServerLevel serverLevel) {
            NetherRaid netherRaid = new NetherRaid(serverLevel,event.getPos());
            AftermathManager instance = AftermathManager.getInstance();
            instance.create(netherRaid, serverLevel, event.getPos(), null);
        }
    }

    @SubscribeEvent
    public static void onRaidStart(AftermathEvent.Start event) {
        System.out.println(event.getPlayers());
        event.getPlayers().forEach(uuid -> {
            Player player = event.getLevel().getPlayerByUUID(uuid);
            if (player != null) {
                player.displayClientMessage(Component.translatable(NETHER_RAID_START), true);
            }
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                level.playLocalSound(player.getX(), player.getY(), player.getZ(),
                        SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2).get(), SoundSource.NEUTRAL, 3.0F, 1.0F, false);
            }
        });
    }

    @SubscribeEvent
    public static void onRaidVictory(AftermathEvent.Victory event) {
        event.getPlayers().forEach(uuid -> {
            Player player = event.getLevel().getPlayerByUUID(uuid);
            ClientLevel level = Minecraft.getInstance().level;
            if (level != null) {
                level.playLocalSound(player.getX(), player.getY(), player.getZ(),
                        ModSoundEvents.ORCHELIAS_VOX.get(), SoundSource.NEUTRAL, 3.0F, 1.0F, false);
            }
        });
    }

    @SubscribeEvent
    public static void joinRaid(EntityJoinLevelEvent event) {
        Entity entity = event.getEntity();
        if (entity instanceof MagmaCube magmaCube) {
            AftermathManager.getInstance().getAftermathMap().values().stream()
                    .filter(aftermath -> aftermath instanceof NetherRaid)
                    .map(aftermath -> (NetherRaid) aftermath)
                    .forEach(raid -> raid.join(entity));
        }
    }
}