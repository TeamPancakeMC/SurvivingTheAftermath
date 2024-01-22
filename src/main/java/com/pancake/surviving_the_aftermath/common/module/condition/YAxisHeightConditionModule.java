package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class YAxisHeightConditionModule extends LevelConditionModule{
    public static final String IDENTIFIER = "y_axis_height_condition";
    public static final Codec<YAxisHeightConditionModule> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("y_axis_height").forGetter(YAxisHeightConditionModule::getYAxisHeight),
            Codec.INT.fieldOf("flag").forGetter(YAxisHeightConditionModule::getFlag)
    ).apply(instance, YAxisHeightConditionModule::new));
    public int yAxisHeight;
    public int flag = 0;

    public YAxisHeightConditionModule(int yAxisHeight, int flag) {
        this.yAxisHeight = yAxisHeight;
        this.flag = flag;
    }

    public YAxisHeightConditionModule() {
    }

    @Override
    public boolean checkCondition(Level level, BlockPos pos) {
        if (flag == 0) {
            return pos.getY() == yAxisHeight;
        } else if (flag == 1) {
            return pos.getY() > yAxisHeight;
        } else if (flag == -1) {
            return pos.getY() < yAxisHeight;
        }
        return false;
    }

    public int getYAxisHeight() {
        return yAxisHeight;
    }

    public int getFlag() {
        return flag;
    }

    @Override
    public Codec<? extends IConditionModule> codec() {
        return CODEC;
    }

    @Override
    public IConditionModule type() {
        return ModAftermathModule.Y_AXIS_HEIGHT_CONDITION.get();
    }

    public static class Builder {
        private final int yAxisHeight;
        private int flag;

        public Builder(int yAxisHeight) {
            this.yAxisHeight = yAxisHeight;
        }
        public Builder flag(int flag) {
            this.flag = flag;
            return this;
        }
        public YAxisHeightConditionModule build() {
            return new YAxisHeightConditionModule(yAxisHeight, flag);
        }
    }
}
