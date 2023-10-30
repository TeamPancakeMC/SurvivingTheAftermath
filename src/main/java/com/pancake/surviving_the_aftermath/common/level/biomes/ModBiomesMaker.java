package com.pancake.surviving_the_aftermath.common.level.biomes;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;

public class ModBiomesMaker {

    public static Biome generateBarren(BiomeGenerationSettings.Builder builder) {
        Biome.BiomeBuilder biomeBuilder = new Biome.BiomeBuilder();
        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder();
        MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
        effects.waterColor(4159204).waterFogColor(329011).fogColor(12638463)
                .skyColor(calculateSkyColor(2.0F))
                .ambientParticle(new AmbientParticleSettings(ParticleTypes.WHITE_ASH, 0.118093334F));
        spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
                EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4));
        BiomeDefaultFeatures.commonSpawns(spawnInfo);
        biomeBuilder.hasPrecipitation(true).temperature(0.8F).downfall(0.9F)
                .specialEffects(effects.build()).mobSpawnSettings(spawnInfo.build()).generationSettings(builder.build())
                .temperatureAdjustment(Biome.TemperatureModifier.NONE).build();
        return biomeBuilder.build();
    }

    protected static int calculateSkyColor(float p_194844_) {
        float $$1 = p_194844_ / 3.0F;
        $$1 = Mth.clamp($$1, -1.0F, 1.0F);
        return Mth.hsvToRgb(0.62222224F - $$1 * 0.05F, 0.5F + $$1 * 0.1F, 1.0F);
    }

}