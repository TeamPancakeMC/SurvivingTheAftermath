package com.pancake.surviving_the_aftermath.common.module.condition;

import com.mojang.serialization.Codec;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.util.RegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class StructureConditionModule extends LevelConditionModule{
    public static final String IDENTIFIER = "structure_condition";
    public static final Codec<StructureConditionModule> CODEC = Codec.STRING.xmap(StructureConditionModule::new, StructureConditionModule::getStructure);
    public String structure;

    public StructureConditionModule(String structure) {
        this.structure = structure;
    }

    public StructureConditionModule() {
    }

    @Override
    public boolean checkCondition(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            ResourceKey<Structure> key = getResourceKey();
            return serverLevel.structureManager().getAllStructuresAt(pos)
                    .containsKey(level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(key));
        }
        return false;
    }

    @NotNull
    public ResourceKey<Structure> getResourceKey() {
        return RegistryUtil.keyStructure(structure);
    }

    public ResourceLocation getResourceLocation() {
        return Objects.requireNonNull(ResourceLocation.tryParse(structure));
    }


    @Override
    public Codec<? extends IConditionModule> codec() {
        return CODEC;
    }

    @Override
    public IConditionModule type() {
        return ModAftermathModule.LEVEL_CONDITION.get();
    }

    public String getStructure() {
        return structure;
    }

    public static class Builder {
        private final String structure;

        public Builder(String structure) {
            this.structure = structure;
        }
        public StructureConditionModule build() {
            return new StructureConditionModule(structure);
        }
    }
}
