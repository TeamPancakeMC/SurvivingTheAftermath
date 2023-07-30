package mod.surviving_the_aftermath.block;

import mod.surviving_the_aftermath.blockentity.ITickingBlockEntity;
import mod.surviving_the_aftermath.blockentity.PartyBonfireBlockEntity;
import mod.surviving_the_aftermath.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CampfireBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class PartyBonfireBlock extends CampfireBlock {

    public PartyBonfireBlock() {
        super(false, 0, BlockBehaviour.Properties.copy(Blocks.CAMPFIRE));
    }

    @Nonnull @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        return InteractionResult.PASS;
    }

    @Nullable @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new PartyBonfireBlockEntity(pos, state);
    }

    @Nullable @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> tile) {
        return ITickingBlockEntity.createTickerHelper(tile, ModBlockEntities.PARTY_BONFIRE.get());
    }

}