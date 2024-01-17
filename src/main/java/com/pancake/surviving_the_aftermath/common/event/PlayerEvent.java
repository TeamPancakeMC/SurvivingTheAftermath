package com.pancake.surviving_the_aftermath.common.event;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.util.StructureUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BeaconBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NetherPortalBlock;
import net.minecraft.world.level.block.entity.BeaconBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mod.EventBusSubscriber
public class PlayerEvent {
    private static Object blockPos;

    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        Player player = event.getEntity();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return;
        }

        BlockPos pos = event.getPos();


        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof BeaconBlockEntity && player.getMainHandItem() == Items.NETHER_STAR.getDefaultInstance()) {
            BeaconBlockEntity beaconBlockEntity = (BeaconBlockEntity) blockEntity;
            if (!beaconBlockEntity.getBeamSections().isEmpty()) {
                player.getMainHandItem().shrink(1);


            }
        }
    }

    public static void createPortal(LevelAccessor world, BlockPos pos) {
        // Try to find an empty portal shape on the X axis
//        Optional<PortalShape> optional = PortalShape.findEmptyPortalShape(world, pos.above(), Direction.Axis.X);
////        System.out.println("optional = " + optional);
////        optional = net.minecraftforge.event.ForgeEventFactory.onTrySpawnPortal(world, pos.above(), optional);
////        System.out.println("optional = " + optional);
//        if (optional.isPresent()) {
//            optional.get().createPortalBlocks();
//            return;
//        }
    }



}
