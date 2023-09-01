package mod.surviving_the_aftermath.blockentity;

import mod.surviving_the_aftermath.init.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import java.util.stream.Stream;

public class PartyBonfireBlockEntity extends BlockEntity implements ITickingBlockEntity{
    private final int RANGE = 2;
    public PartyBonfireBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.PARTY_BONFIRE.get(), pPos, pBlockState);
    }
    public void tick() {
        if (level != null && !level.isClientSide) {
            AABB aabb = new AABB(getBlockPos()).inflate(RANGE);
            Stream<Player> players = level.getEntitiesOfClass(Player.class, aabb).stream().parallel();
            players.forEach(player -> player.addEffect(new MobEffectInstance(MobEffects.REGENERATION, 20 * 15, 0)));
        }
    }
}

