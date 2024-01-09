package com.pancake.surviving_the_aftermath.common.data.datagen.raid;

import com.pancake.surviving_the_aftermath.common.data.datagen.AftermathModuleProviders;
import com.pancake.surviving_the_aftermath.common.module.amount.IntegerAmountModule;
import com.pancake.surviving_the_aftermath.common.module.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoWithEquipmentModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import net.minecraft.data.PackOutput;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;

import java.util.List;

public class NetherRaidModuleProvider extends AftermathModuleProviders {
    public NetherRaidModuleProvider(PackOutput output) {
        super(output, "surviving_the_aftermath");
    }

    @Override
    public void addModules() {
        RandomAmountModule randomAmountModule45 = new RandomAmountModule.Builder(4, 5).build();
        RandomAmountModule randomAmountModule12 = new RandomAmountModule.Builder(1, 2).build();
        IntegerAmountModule integerAmountModule1 = new IntegerAmountModule.Builder(1).build();


        ItemWeightedModule itemGoldenModule = new ItemWeightedModule.Builder()
                .add("minecraft:golden_helmet", 1)
                .add("minecraft:golden_chestplate", 1)
                .add("minecraft:golden_leggings", 1)
                .add("minecraft:golden_boots", 1)
                .add("minecraft:golden_sword", 1)
                .build();


        NetherRaidModule netherRaidModule = new NetherRaidModule.Builder("common")
                .setReadyTime(100)
                /*   1   */
                .addWave(List.of(new EntityInfoModule.Builder("minecraft:zombified_piglin")
                                .amountModule(randomAmountModule45).build()
                        )
                )
                /*   2   */
                .addWave(List.of(
                        new EntityInfoModule.Builder("minecraft:zombified_piglin")
                                .amountModule(randomAmountModule45).build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(integerAmountModule1).build()
                ))
                /*   3   */
                .addWave(List.of(
                        new EntityInfoModule.Builder("minecraft:piglin")
                                .amountModule(randomAmountModule45).build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(integerAmountModule1).build(),
                        new EntityInfoModule.Builder("minecraft:magma_cube")
                                .amountModule(integerAmountModule1).build()
                ))
                /*   4   */
                .addWave(List.of(
                        new EntityInfoModule.Builder("minecraft:piglin")
                                .amountModule(randomAmountModule45).build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(randomAmountModule12).build(),
                        new EntityInfoModule.Builder("minecraft:magma_cube")
                                .amountModule(integerAmountModule1).build(),
                        new EntityInfoModule.Builder("minecraft:blaze")
                                .amountModule(integerAmountModule1).build()
                ))
                /*   5   */
                .addWave(List.of(
                        new EntityInfoModule.Builder("minecraft:piglin")
                                .amountModule(randomAmountModule45).build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(randomAmountModule12).build(),
                        new EntityInfoModule.Builder("minecraft:magma_cube")
                                .amountModule(randomAmountModule12).build(),
                        new EntityInfoModule.Builder("minecraft:blaze")
                                .amountModule(integerAmountModule1).build(),
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin_brute")
                                .equipment(itemGoldenModule)
                                .amountModule(integerAmountModule1).build()
                ))
                /*   6   */
                .addWave(List.of(
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin")
                                .equipment(itemGoldenModule)
                                .amountModule(new RandomAmountModule.Builder(3, 4).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(new RandomAmountModule.Builder(2, 3).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:magma_cube")
                                .amountModule(new RandomAmountModule.Builder(1, 2).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:ghast")
                                .amountModule((new RandomAmountModule.Builder(1, 3).build()))
                                .build(),
                        new EntityInfoModule.Builder("minecraft:blaze")
                                .amountModule(integerAmountModule1).build(),
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin_brute")
                                .equipment(itemGoldenModule)
                                .amountModule(integerAmountModule1).build()
                ))
                /*   7   */
                .addWave(List.of(
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin")
                                .equipment(itemGoldenModule)
                                .amountModule(new RandomAmountModule.Builder(3, 4).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(new RandomAmountModule.Builder(3, 4).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:magma_cube")
                                .amountModule(new RandomAmountModule.Builder(2, 4).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:ghast")
                                .amountModule(new RandomAmountModule.Builder(2, 4).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:blaze")
                                .amountModule(integerAmountModule1)
                                .build(),
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin_brute")
                                .equipment(itemGoldenModule)
                                .amountModule(integerAmountModule1)
                                .build()

                ))
                /*   8   */
                .addWave(List.of(
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin")
                                .equipment(itemGoldenModule)
                                .amountModule(new RandomAmountModule.Builder(3,4).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(new RandomAmountModule.Builder(2,4).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:magma_cube")
                                .amountModule(new RandomAmountModule.Builder(3,4).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:ghast")
                                .amountModule(new RandomAmountModule.Builder(2,6).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:blaze")
                                .amountModule(new IntegerAmountModule.Builder(4).build())
                                .build(),
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin_brute")
                                .equipment(itemGoldenModule)
                                .amountModule(new IntegerAmountModule.Builder(5).build())
                                .build()
                ))
                /*   9   */
                .addWave(List.of(
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin")
                                .equipment(itemGoldenModule)
                                .amountModule(new RandomAmountModule.Builder(4, 6).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(new RandomAmountModule.Builder(3,6).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:magma_cube")
                                .amountModule(new RandomAmountModule.Builder(3,5).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:ghast")
                                .amountModule(new RandomAmountModule.Builder(3,5).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:blaze")
                                .amountModule(new IntegerAmountModule.Builder(4).build())
                                .build(),
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin_brute")
                                .equipment(itemGoldenModule)
                                .amountModule(new IntegerAmountModule.Builder(6).build())
                                .build()
                ))
                /*   10   */
                .addWave(List.of(
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin")
                                .equipment(itemGoldenModule)
                                .amountModule(new RandomAmountModule.Builder(2, 6).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(new RandomAmountModule.Builder(2, 6).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:magma_cube")
                                .amountModule(new RandomAmountModule.Builder(2, 6).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:ghast")
                                .amountModule(new RandomAmountModule.Builder(2, 6).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:blaze")
                                .amountModule(new IntegerAmountModule.Builder(6).build())
                                .build(),
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin_brute")
                                .equipment(itemGoldenModule)
                                .amountModule(new RandomAmountModule.Builder(3,5).build())
                                .build()
                ))
                /*   11   */
                .addWave(List.of(
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin")
                                .equipment(itemGoldenModule)
                                .amountModule(new RandomAmountModule.Builder(1, 8).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:hoglin")
                                .amountModule(new RandomAmountModule.Builder(1, 8).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:magma_cube")
                                .amountModule(new RandomAmountModule.Builder(1, 8).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:ghast")
                                .amountModule(new RandomAmountModule.Builder(1, 8).build())
                                .build(),
                        new EntityInfoModule.Builder("minecraft:blaze")
                                .amountModule(new RandomAmountModule.Builder(1, 8).build())
                                .build(),
                        new EntityInfoWithEquipmentModule.Builder("minecraft:piglin_brute")
                                .equipment(new ItemWeightedModule.Builder()
                                        .add("minecraft:netherite_helmet", 1)
                                        .add("minecraft:netherite_chestplate", 1)
                                        .add("minecraft:netherite_leggings", 1)
                                        .add("minecraft:netherite_boots", 1)
                                        .add("minecraft:netherite_sword", 1)
                                        .build()
                                )
                                .amountModule(new IntegerAmountModule.Builder(8).build())
                                .build()
                ))
                .reward(new ItemWeightedModule.Builder()
                        .add("minecraft:gold_ingot", 100)
                        .add("minecraft:diamond", 10)
                        .add("minecraft:emerald",20)
                        .add("minecraft:enchanted_golden_apple",2)
                        .add("minecraft:nether_scrap",2)
                        .add("surviving_the_aftermath:nether_core",1)
                        .build())
                .build();
        addModule(netherRaidModule);

    }
}
