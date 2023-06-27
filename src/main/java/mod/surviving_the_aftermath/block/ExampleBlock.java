package mod.surviving_the_aftermath.block;

import mod.surviving_the_aftermath.blockentity.ExampleBlockEntity;
import mod.surviving_the_aftermath.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class ExampleBlock extends Block implements EntityBlock {
	public ExampleBlock(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
		return new ExampleBlockEntity(pPos, pState);
	}

	@Override
	public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState,
			BlockEntityType<T> pBlockEntityType) {
		return pBlockEntityType == ModBlockEntities.EXAMPLE.get()
				? (level, pos, state, tileEntity) -> ((ExampleBlockEntity) tileEntity).tick()
				: null;
	}

}
