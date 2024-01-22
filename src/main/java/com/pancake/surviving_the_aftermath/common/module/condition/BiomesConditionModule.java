package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.ForgeRegistries;

public class BiomesConditionModule extends LevelConditionModule{
    public static final String IDENTIFIER = "biomes_condition";
    public static final Codec<BiomesConditionModule> CODEC = Codec.STRING.xmap(BiomesConditionModule::new, BiomesConditionModule::getBiomes);
    public String biomes;

    public BiomesConditionModule(String biomes) {
        this.biomes = biomes;
    }

    public BiomesConditionModule() {
    }

    public String getBiomes() {
        return biomes;
    }

    @Override
    public boolean checkCondition(Level level, BlockPos pos) {
        Biome biome = ForgeRegistries.BIOMES.getValue(ResourceLocation.tryParse(biomes));
        return level.getBiome(pos).get() == biome;
    }

    @Override
    public Codec<? extends IConditionModule> codec() {
        return CODEC;
    }

    @Override
    public IConditionModule type() {
        return ModAftermathModule.BIOMES_CONDITION.get();
    }

    public static class Builder {
        private final String biomes;

        public Builder (String biomes) {
            this.biomes = biomes;
        }
        public BiomesConditionModule build() {
            return new BiomesConditionModule(biomes);
        }
    }
}
