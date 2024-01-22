package com.pancake.surviving_the_aftermath.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MoveControl.class)
public abstract class MoveControlMixin {
    @Accessor
    abstract public void setWantedX(double wantedX);
    @Accessor
    abstract public void setWantedY(double wantedY);
    @Accessor
    abstract public void setWantedZ(double wantedZ);
    @Accessor
    abstract public void setSpeedModifier(double speedModifier);
    @Inject(method = "setWantedPosition", at = @At("RETURN"))
    private void setWantedPosition(double p_24984_, double p_24985_, double p_24986_, double p_24987_, CallbackInfo ci) {
        CompoundTag persistentData = getMob().getPersistentData();
        if (persistentData.contains("restricted_range")) {
            BlockPos restrictedRange = NbtUtils.readBlockPos((CompoundTag) persistentData.get("restricted_range"));
            setWantedX(restrictedRange.getX());
            setWantedY(restrictedRange.getY());
            setWantedZ(restrictedRange.getZ());
            setSpeedModifier(p_24987_);
        }
    }
    @Accessor
    abstract public Mob getMob();
}