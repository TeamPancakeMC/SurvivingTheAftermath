package com.pancake.surviving_the_aftermath.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.SpawnUtil;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;

import java.util.Optional;

public class SpawnEntityUtil {
   public static Optional<Entity> trySpawnMob(EntityType<?> entityType, ServerLevel serverLevel, BlockPos pos, int attemptCount, int offsetRadius, int p_216410_, SpawnUtil.Strategy strategy) {
      BlockPos.MutableBlockPos blockpos$mutableblockpos = pos.mutable();

      for(int i = 0; i < attemptCount; ++i) {
         int j = Mth.randomBetweenInclusive(serverLevel.random, -offsetRadius, offsetRadius);
         int k = Mth.randomBetweenInclusive(serverLevel.random, -offsetRadius, offsetRadius);
         blockpos$mutableblockpos.setWithOffset(pos, j, p_216410_, k);
         if (serverLevel.getWorldBorder().isWithinBounds(blockpos$mutableblockpos) && moveToPossibleSpawnPosition(serverLevel, p_216410_, blockpos$mutableblockpos, strategy)) {
            Entity entity = entityType.create(serverLevel);
            if (entity != null) {
                entity.moveTo(blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getY(), blockpos$mutableblockpos.getZ(), serverLevel.random.nextFloat() * 360.0F, 0.0F);
               serverLevel.addFreshEntityWithPassengers(entity);
               return Optional.of(entity);
            }
         }
      }

      return Optional.empty();
   }

   private static boolean moveToPossibleSpawnPosition(ServerLevel p_216399_, int p_216400_, BlockPos.MutableBlockPos p_216401_, SpawnUtil.Strategy p_216402_) {
      BlockPos.MutableBlockPos blockpos$mutableblockpos = (new BlockPos.MutableBlockPos()).set(p_216401_);
      BlockState blockstate = p_216399_.getBlockState(blockpos$mutableblockpos);

      for(int i = p_216400_; i >= -p_216400_; --i) {
         p_216401_.move(Direction.DOWN);
         blockpos$mutableblockpos.setWithOffset(p_216401_, Direction.UP);
         BlockState blockstate1 = p_216399_.getBlockState(p_216401_);
         if (p_216402_.canSpawnOn(p_216399_, p_216401_, blockstate1, blockpos$mutableblockpos, blockstate)) {
            p_216401_.move(Direction.UP);
            return true;
         }

         blockstate = blockstate1;
      }

      return false;
   }
}