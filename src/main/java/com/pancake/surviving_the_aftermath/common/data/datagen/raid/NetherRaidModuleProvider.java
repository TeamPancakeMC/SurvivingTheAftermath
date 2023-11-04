package com.pancake.surviving_the_aftermath.common.data.datagen.raid;

import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.impl.amount.FixedAmountModule;
import com.pancake.surviving_the_aftermath.api.module.impl.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.api.module.impl.entity_info.EntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.common.data.datagen.AftermathModuleProviders;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import net.minecraft.data.PackOutput;
import org.apache.commons.compress.utils.Lists;

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
                            .setEntityType("minecraft:zombie")
                            .setAmountModule(() -> new FixedAmountModule.Builder()
                                    .setAmount(10)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);
                    return infoModules;
                })
                .addWave(() -> {
                    List<IEntityInfoModule> infoModules = Lists.newArrayList();
                    EntityInfoModule infoModule = new EntityInfoModule.Builder()
                            .setEntityType("minecraft:creeper")
                            .setAmountModule(() -> new RandomAmountModule.Builder()
                                    .setRange(10, 20)
                                    .build()
                            ).build();
                    infoModules.add(infoModule);
                    return infoModules;
                })
                .setRewards(() -> new ItemWeightedListModule.Builder()
                        .add("minecraft:stone", 1)
                        .add("minecraft:diamond", 1)
                        .build())
                .build();
        addModule(netherRaidModule);
    }
}
