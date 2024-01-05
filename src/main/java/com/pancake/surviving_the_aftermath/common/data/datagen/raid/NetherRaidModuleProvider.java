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
import net.minecraft.world.item.Items;

import java.util.List;

public class NetherRaidModuleProvider extends AftermathModuleProviders<NetherRaidModule> {
    public NetherRaidModuleProvider(PackOutput output) {
        super(output, "surviving_the_aftermath");
    }


    @Override
    public void addModules() {
        NetherRaidModule netherRaidModule = new NetherRaidModule.Builder("common")
                .readyTime(100)
                .addWave(List.of(new EntityInfoModule(EntityType.PIG, new IntegerAmountModule(10)),
                        new EntityInfoWithEquipmentModule(EntityType.ZOMBIE, new RandomAmountModule(10, 20),
                                new ItemWeightedModule(List.of(WeightedEntry.wrap(Items.IRON_SWORD, 1),
                                        WeightedEntry.wrap(Items.IRON_AXE, 1),
                                        WeightedEntry.wrap(Items.IRON_PICKAXE, 1)
                                )
                                )
                        )
                ))
                .build();

        addModule(netherRaidModule);
    }
}
