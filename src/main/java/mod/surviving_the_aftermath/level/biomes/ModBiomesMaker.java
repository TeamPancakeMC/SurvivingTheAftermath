package mod.surviving_the_aftermath.level.biomes;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.*;

public class ModBiomesMaker {

    public static Biome generateBarren(BiomeGenerationSettings.Builder builder) {
        Biome.BiomeBuilder biomeBuilder = new Biome.BiomeBuilder();
        BiomeSpecialEffects.Builder effects = new BiomeSpecialEffects.Builder();
        MobSpawnSettings.Builder spawnInfo = new MobSpawnSettings.Builder();
        effects.waterColor(4159204).waterFogColor(329011).fogColor(12638463)
                .skyColor(OverworldBiomes.calculateSkyColor(2.0F))
                .ambientParticle(new AmbientParticleSettings(ParticleTypes.WHITE_ASH, 0.118093334F));
        spawnInfo.addSpawn(MobCategory.MONSTER, new MobSpawnSettings.SpawnerData(
                EntityType.ZOMBIFIED_PIGLIN, 100, 4, 4));
        BiomeDefaultFeatures.commonSpawns(spawnInfo);
        biomeBuilder.hasPrecipitation(true).temperature(0.8F).downfall(0.9F)
                .specialEffects(effects.build()).mobSpawnSettings(spawnInfo.build()).generationSettings(builder.build())
                .temperatureAdjustment(Biome.TemperatureModifier.NONE).build();
        return biomeBuilder.build();
    }

}