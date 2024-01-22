package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class WeatherConditionModule extends LevelConditionModule{
    public static final String IDENTIFIER = "weather_condition";
    public static final Codec<WeatherConditionModule> CODEC = Codec.STRING.xmap(WeatherConditionModule::new, WeatherConditionModule::getWeather);
    public String weather;

    public WeatherConditionModule(String weather) {
        this.weather = weather;
    }
    public WeatherConditionModule() {
    }

    @Override
    public boolean checkCondition(Level level, BlockPos pos) {
        if (level.isRaining()) {
            return weather.equals("rain");
        } else if (level.isThundering()) {
            return weather.equals("thunder");
        } else {
            return weather.equals("clear");
        }
    }

    @Override
    public Codec<? extends IConditionModule> codec() {
        return CODEC;
    }

    @Override
    public IConditionModule type() {
        return ModAftermathModule.WEATHER_CONDITION.get();
    }

    public String getWeather() {
        return weather;
    }

    public static class Builder {
        private final String weather;

        public Builder(String weather) {
            this.weather = weather;
        }
        public WeatherConditionModule build() {
            return new WeatherConditionModule(weather);
        }
    }
}
