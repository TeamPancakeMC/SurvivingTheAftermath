package mod.surviving_the_aftermath.capability;

import mod.surviving_the_aftermath.init.ModCapability;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;



public class PlayerBattleCapability implements ICapabilityProvider, ICapabilitySerializable<CompoundTag> {
    private int deathCount = 0;
    private Long lastEscapeTime = 0L;


    public int getDeathCount() {
        return deathCount;
    }

    public void setDeathCount(int deathCount) {
        this.deathCount = deathCount;
    }
    public Long getLastEscapeTime() {
        return lastEscapeTime;
    }

    public void setLastEscapeTime(Long lastEscapeTime) {
        this.lastEscapeTime = lastEscapeTime;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return ModCapability.PLAYER_BATTLE.orEmpty(cap, LazyOptional.of(() -> this));
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putInt("deathCount", deathCount);
        compoundTag.putLong("lastEscapeTime", lastEscapeTime);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.deathCount = nbt.getInt("deathCount");
        this.lastEscapeTime = nbt.getLong("lastEscapeTime");
    }
}
