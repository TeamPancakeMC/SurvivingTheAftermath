package mod.surviving_the_aftermath.block;

import mod.surviving_the_aftermath.blockentity.ITickingBlockEntity;
import mod.surviving_the_aftermath.blockentity.PartyBonfireBlockEntity;
import mod.surviving_the_aftermath.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class PartyBonfireBlock extends Block implements EntityBlock {
    public PartyBonfireBlock() {
        super(BlockBehaviour.Properties.of());
    }
    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new PartyBonfireBlockEntity(pPos, pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> tile) {
        return ITickingBlockEntity.createTickerHelper(tile, ModBlockEntities.PARTY_BONFIRE.get());
    }
}
