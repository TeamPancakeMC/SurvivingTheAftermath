package com.pancake.surviving_the_aftermath.common.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Set;

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
    //返回
    @Inject(method = "setWantedPosition", at = @At("RETURN"))
    private void setWantedPosition(double p_24984_, double p_24985_, double p_24986_, double p_24987_, CallbackInfo ci) {
        Set<String> tags = getMob().getTags();
        if (!tags.isEmpty()) {
            String tag = tags.iterator().next();
            if (tag.contains("restricted_range")) {
                String[] split = tag.split(":");
                if (split.length == 2) {
                    String[] pos = split[1].split(", ");
                    if (pos.length == 3) {
                        setWantedX(Integer.parseInt(pos[0]));
                        setWantedY(Integer.parseInt(pos[1]));
                        setWantedZ(Integer.parseInt(pos[2]));
                        setSpeedModifier(p_24987_);
                        getMob().removeTag(tag);
                    }
                }
            }
        }
    }

    @Accessor
    abstract public Mob getMob();
}
