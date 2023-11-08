package com.pancake.surviving_the_aftermath.common.data.datagen.raid;

import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.impl.amount.FixedAmountModule;
import com.pancake.surviving_the_aftermath.api.module.impl.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.api.module.impl.entity_info.EntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.impl.entity_info.EntityInfoWithEquipmentModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.common.data.datagen.AftermathModuleProviders;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import net.minecraft.data.PackOutput;

import java.util.List;

public class NetherRaidModuleProvider extends AftermathModuleProviders<NetherRaidModule> {
    private static final AftermathAPI AFTERMATH_API = AftermathAPI.getInstance();
    public NetherRaidModuleProvider(PackOutput output) {
        super(output, "surviving_the_aftermath");
    }


    @Override
    public void addModules() {
        NetherRaidModule netherRaidModule = new NetherRaidModule.Builder("common")
                .setReadyTime(100)
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoModule infoModule = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(4,5)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoModule infoModule = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:zombified_piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(4, 5)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(4, 5)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoModule infoModule = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(4, 5)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();
                    infoModules.add(infoModule2);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoModule infoModule = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(4, 5)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();
                    infoModules.add(infoModule3);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoWithEquipmentModule infoModule = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:golden_helmet",1)
                                        .add("minecraft:golden_chestplate",1)
                                        .add("minecraft:golden_leggings",1)
                                        .add("minecraft:golden_boots",1)
                                        .add("minecraft:golden_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(2,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:ghast")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,3)
                                    .build()
                            ).build();
                    EntityInfoModule infoModule4 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();

                    infoModules.add(infoModule4);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoModule infoModule = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();
                    EntityInfoWithEquipmentModule infoModule4 = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:golden_helmet",1)
                                        .add("minecraft:golden_chestplate",1)
                                        .add("minecraft:golden_leggings",1)
                                        .add("minecraft:golden_boots",1)
                                        .add("minecraft:golden_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin_brute")
                            .setAmountModule(() -> new  FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();
                    infoModules.add(infoModule4);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoWithEquipmentModule infoModule = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:golden_helmet",1)
                                        .add("minecraft:golden_chestplate",1)
                                        .add("minecraft:golden_leggings",1)
                                        .add("minecraft:golden_boots",1)
                                        .add("minecraft:golden_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(2,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    infoModules.add(infoModule2);
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:ghast")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule3);
                    EntityInfoModule infoModule4 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();

                    infoModules.add(infoModule4);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoWithEquipmentModule infoModule = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:golden_helmet",1)
                                        .add("minecraft:golden_chestplate",1)
                                        .add("minecraft:golden_leggings",1)
                                        .add("minecraft:golden_boots",1)
                                        .add("minecraft:golden_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(2,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    infoModules.add(infoModule2);
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:ghast")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule3);
                    EntityInfoModule infoModule4 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();

                    infoModules.add(infoModule4);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoWithEquipmentModule infoModule = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:golden_helmet",1)
                                        .add("minecraft:golden_chestplate",1)
                                        .add("minecraft:golden_leggings",1)
                                        .add("minecraft:golden_boots",1)
                                        .add("minecraft:golden_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(2,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    infoModules.add(infoModule2);
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:ghast")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule3);
                    EntityInfoModule infoModule4 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();

                    infoModules.add(infoModule4);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoWithEquipmentModule infoModule = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:golden_helmet",1)
                                        .add("minecraft:golden_chestplate",1)
                                        .add("minecraft:golden_leggings",1)
                                        .add("minecraft:golden_boots",1)
                                        .add("minecraft:golden_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(2,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    infoModules.add(infoModule2);
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:ghast")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule3);
                    EntityInfoModule infoModule4 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();

                    infoModules.add(infoModule4);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoWithEquipmentModule infoModule = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:golden_helmet",1)
                                        .add("minecraft:golden_chestplate",1)
                                        .add("minecraft:golden_leggings",1)
                                        .add("minecraft:golden_boots",1)
                                        .add("minecraft:golden_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(2,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    infoModules.add(infoModule2);
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:ghast")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule3);
                    EntityInfoModule infoModule4 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();

                    infoModules.add(infoModule4);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoWithEquipmentModule infoModule = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:golden_helmet",1)
                                        .add("minecraft:golden_chestplate",1)
                                        .add("minecraft:golden_leggings",1)
                                        .add("minecraft:golden_boots",1)
                                        .add("minecraft:golden_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(2,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    infoModules.add(infoModule2);
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:ghast")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule3);
                    EntityInfoModule infoModule4 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();

                    infoModules.add(infoModule4);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoWithEquipmentModule infoModule = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:golden_helmet",1)
                                        .add("minecraft:golden_chestplate",1)
                                        .add("minecraft:golden_leggings",1)
                                        .add("minecraft:golden_boots",1)
                                        .add("minecraft:golden_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);

                    EntityInfoModule infoModule1 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:hoglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(2,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule1);

                    EntityInfoModule infoModule2 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:magma_cube")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,2)
                                    .build()
                            ).build();
                    infoModules.add(infoModule2);
                    EntityInfoModule infoModule3 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:ghast")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(1,3)
                                    .build()
                            ).build();
                    infoModules.add(infoModule3);
                    EntityInfoModule infoModule4 = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:blaze")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(1)
                                    .build()
                            ).build();

                    infoModules.add(infoModule4);
                    EntityInfoWithEquipmentModule infoModule5 = (EntityInfoWithEquipmentModule) new EntityInfoWithEquipmentModule.Builder()
                            .setEquipment(() -> {
                                return new ItemWeightedListModule.Builder()
                                        .add("minecraft:netherite_helmet",1)
                                        .add("minecraft:netherite_chestplate",1)
                                        .add("minecraft:netherite_leggings",1)
                                        .add("minecraft:netherite_boots",1)
                                        .add("minecraft:netherite_sword",1)
                                        .build();

                            })
                            .setEntityType("minecraft:piglin")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(3,4)
                                    .build()
                            ).build();
                    infoModules.add(infoModule5);
                    return infoModules;
                })
                .setRewards(() -> new ItemWeightedListModule.Builder()
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
