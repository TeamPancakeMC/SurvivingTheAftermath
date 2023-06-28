package mod.surviving_the_aftermath.blockentity;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;

public interface ITickingBlockEntity {

    void tick();

    static <E extends BlockEntity, A extends BlockEntity> BlockEntityTicker<A> createTickerHelper(BlockEntityType<A> actual, BlockEntityType<E> expected) {
        return expected == actual ? (level, pos, state, tile) -> ((ITickingBlockEntity) tile).tick() : null;
    }
}