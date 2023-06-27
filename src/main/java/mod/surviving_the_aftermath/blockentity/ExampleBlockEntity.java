package mod.surviving_the_aftermath.blockentity;

import mod.surviving_the_aftermath.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlockEntity extends BlockEntity {

	public ExampleBlockEntity(BlockPos pPos, BlockState pBlockState) {
		super(ModBlockEntities.EXAMPLE.get(), pPos, pBlockState);
	}

	public void tick() {
		if (level == null || level.isClientSide)
			return;

		if (level.getGameTime() % 20 == 0) {
			var player = level.getNearestPlayer(getBlockPos().getX(), getBlockPos().getY(), getBlockPos().getZ(), 10,
					null);
			if (player != null) {
				player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 40, 0));
			}
		}
	}
}
