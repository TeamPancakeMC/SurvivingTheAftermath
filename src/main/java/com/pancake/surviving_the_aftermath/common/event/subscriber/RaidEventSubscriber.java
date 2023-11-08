package com.pancake.surviving_the_aftermath.common.event.subscriber;

import com.pancake.surviving_the_aftermath.api.aftermath.AftermathManager;
import com.pancake.surviving_the_aftermath.common.event.AftermathEvent;
import com.pancake.surviving_the_aftermath.common.init.ModSoundEvents;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;
import com.pancake.surviving_the_aftermath.common.raid.api.BaseRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Objects;


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
            PortalShape.findEmptyPortalShape(serverLevel, event.getPos(), Direction.Axis.X).ifPresent(portalShape -> {
                portalShape.createPortalBlocks();
                NetherRaid netherRaid = new NetherRaid(serverLevel, event.getPos(),portalShape);
                AftermathManager instance = AftermathManager.getInstance();
                if (instance.create(netherRaid)) {
                    event.setCanceled(true);
                }
            });
        }
    }
    @SubscribeEvent
    public static void onRaidStart(AftermathEvent.Start event)
    {
        event.getPlayers().forEach(uuid -> {
            Player player = event.getLevel().getPlayerByUUID(uuid);
            if (player != null) {
                player.displayClientMessage(Component.translatable(NETHER_RAID_START), true);
            }
        });
    }

    @SubscribeEvent
    public static void onRaidVictory(AftermathEvent.Victory event) {
        event.getPlayers().forEach(uuid -> {
            Player player = event.getLevel().getPlayerByUUID(uuid);
            Level level = Objects.requireNonNull(player).level();
            if (level.isClientSide) {
                player.displayClientMessage(Component.translatable(NETHER_RAID_VICTORY), true);
                level.playSeededSound(player, player.getX(), player.getY(), player.getZ(),
                        ModSoundEvents.ORCHELIAS_VOX.get(), SoundSource.NEUTRAL, 3.0F, 1.0F, level.random.nextLong());
            }
        });
    }

    @SubscribeEvent
    public static void joinRaid(EntityJoinLevelEvent event) {
        AftermathManager.getInstance().getAftermathMap().values().stream()
                .filter(aftermath -> aftermath instanceof BaseRaid<?>)
                .map(aftermath -> (BaseRaid<?>) aftermath)
                .forEach(raid -> raid.join(event.getEntity()));
    }



}
