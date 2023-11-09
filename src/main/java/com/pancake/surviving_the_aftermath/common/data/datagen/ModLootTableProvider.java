package com.pancake.surviving_the_aftermath.common.data.datagen;


import com.pancake.surviving_the_aftermath.common.data.datagen.loot.ModBlockLoot;
import com.pancake.surviving_the_aftermath.common.data.datagen.loot.ModChestLoot;
import com.pancake.surviving_the_aftermath.common.data.datagen.loot.ModEntityLoot;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ModLootTableProvider extends LootTableProvider {

    public ModLootTableProvider(PackOutput output) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(ModBlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(ModChestLoot::new, LootContextParamSets.CHEST),
                new SubProviderEntry(ModEntityLoot::new, LootContextParamSets.ENTITY)));
    }

}