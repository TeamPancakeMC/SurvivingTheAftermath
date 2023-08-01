package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.levelgen.NoiseGeneratorSettings;
import net.minecraftforge.registries.DeferredRegister;

public class ModLevels {

    public static final DeferredRegister<DimensionType> DIMENSION_TYPES = DeferredRegister.create(Registries.DIMENSION_TYPE, Main.MODID);
    public static final DeferredRegister<NoiseGeneratorSettings> NOISE_GENERATORS = DeferredRegister.create(Registries.NOISE_SETTINGS, Main.MODID);

    /*
    private static DimensionType modDimensionType() {
        return new DimensionType(OptionalLong.empty(), true, false, false, true,
                1.0D, true, false, -64, 320, 256,
                BlockTags.INFINIBURN_OVERWORLD, BuiltinDimensionTypes.OVERWORLD_EFFECTS, 0.0F,
                new DimensionType.MonsterSettings(false, true, UniformInt.of(0, 7), 0));
    }

    private static NoiseGeneratorSettings modNoiseGeneratorSettings() {
        final NoiseSettings NOISE_SETTINGS = NoiseSettings.create(-64, 256, 2, 1);
        return new NoiseGeneratorSettings(NOISE_SETTINGS, Blocks.STONE.defaultBlockState(), Blocks.WATER.defaultBlockState(),
                NoiseRouterData.overworld(NoiseSettings.OVERWORLD_NOISE_SETTINGS, false, false), NPSurfaceRule.createSurfaceRule(),
                List.of(), 63, false, true, true, false);
    }

    public static SurfaceRules.RuleSource createNormalRuleSource(ResourceKey<Biome> resourceKey, Block surfaceBlock, Block secondBlock, Block underwaterBlock) {
        return SurfaceRules.ifTrue(SurfaceRules.isBiome(resourceKey), SurfaceRules.ifTrue(SurfaceRules.abovePreliminarySurface(),
                SurfaceRules.sequence(SurfaceRules.ifTrue(SurfaceRules.ON_FLOOR, SurfaceRules.sequence(SurfaceRules.ifTrue(
                                SurfaceRules.waterBlockCheck(-1, 0), makeStateRule(surfaceBlock)),
                        makeStateRule(underwaterBlock))), SurfaceRules.ifTrue(SurfaceRules.UNDER_FLOOR, makeStateRule(secondBlock)))));
    }

     */

}