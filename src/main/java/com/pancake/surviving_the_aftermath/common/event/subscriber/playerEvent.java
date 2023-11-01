package com.pancake.surviving_the_aftermath.common.event.subscriber;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.init.ModStructures;
import com.pancake.surviving_the_aftermath.common.structure.NetherRaidStructure;
import com.pancake.surviving_the_aftermath.common.util.StructureUtil;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.portal.PortalShape;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Mod.EventBusSubscriber
public class playerEvent {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        if (level.isClientSide() || hand == InteractionHand.MAIN_HAND) return;

        BlockPos startPos = event.getPos();



//        if (level instanceof ServerLevel serverLevel) {
//            ResourceLocation STRUCTURE = SurvivingTheAftermath.asResource("nether_invasion_portal");
//            Optional<StructureTemplate> Optional = serverLevel.getStructureManager().get(STRUCTURE);
//            if (Optional.isPresent()) {
//                StructureTemplate structureTemplate = Optional.get();
//                StructureUtil.StructureModule structureModule = StructureUtil.getStructureModule(structureTemplate).clearAir();
//                StructureUtil.randomCoordinatesCompareStructure(serverLevel, startPos, structureModule);
//            }
//        }

//        if (level.isClientSide() && hand == InteractionHand.MAIN_HAND) {
//            SimpleParticleType particle = ParticleTypes.PORTAL;
//            //粒子可以从Blockpos1 直线运动到 Blockpos2
//            BlockPos startPos = event.getPos();
//            BlockPos endPos = event.getPos().east(5);
//
//            double startX = startPos.getX() + 0.5; // 加上0.5是为了将粒子的位置设置在方块的中心
//            double startY = startPos.getY() + 0.5;
//            double startZ = startPos.getZ() + 0.5;
//
//            double endX = endPos.getX() + 0.5;
//            double endY = endPos.getY() + 0.5;
//            double endZ = endPos.getZ() + 0.5;
//
//            double distanceX = endX - startX;
//            double distanceY = endY - startY;
//            double distanceZ = endZ - startZ;
//
//            int steps = 100; // 将直线分成100个时间步长
//
//            for (int i = 0; i < steps; i++) {
//                double progress = (double) i / (double) steps; // 计算当前时间步长的进度
//
//                double currentX = startX + (distanceX * progress);
//                double currentY = startY + (distanceY * progress);
//                double currentZ = startZ + (distanceZ * progress);
//
//                // 生成粒子
////                level.addParticle(particle, currentX, currentY, currentZ, 0, 0, 0);
//
//                double sinValue = Math.sin(progress * Math.PI * 2); // 将进度映射到0到2π的范围内
//                currentY += sinValue * 0.5; // 调整振幅和曲线的高度
//
////                level.addParticle(particle, currentX, currentY, currentZ, 0, 0, 0);
//
//
//                double rotation = progress * Math.PI * 2;
//
//                //旋转粒子
//                double x = Math.cos(rotation) * 0.5;
//                double z = Math.sin(rotation) * 0.5;
//
//                int speed = 2;
////                level.addParticle(particle, currentX, currentY, currentZ, x, 0, z);
//
//                //粒子的速度
//                level.addParticle(particle, currentX, currentY, currentZ, x * speed, 0, z * speed);
//
//            }





//        }
    }
}
