package com.pancake.surviving_the_aftermath.common.data.datagen.loot;

import net.minecraft.data.loot.packs.VanillaChestLoot;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootTable;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiConsumer;

@ParametersAreNonnullByDefault
public class ModChestLoot extends VanillaChestLoot {

    @Override
    public void generate(BiConsumer<ResourceLocation, LootTable.Builder> biConsumer) {

    }

}