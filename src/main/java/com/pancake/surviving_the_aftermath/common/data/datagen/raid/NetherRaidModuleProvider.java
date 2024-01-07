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
        RandomAmountModule randomAmountModule = new RandomAmountModule(1, 5);
        IntegerAmountModule integerAmountModule = new IntegerAmountModule(1);
        EntityInfoModule entityInfoModule = new EntityInfoModule(EntityType.PIG, randomAmountModule);
        NetherRaidModule netherRaidModule = new NetherRaidModule();
        ItemWeightedModule itemWeightedModule = new ItemWeightedModule(List.of(
                WeightedEntry.wrap(Items.STONE, 10),
                WeightedEntry.wrap(Items.STONE_AXE, 20)
        ));
        EntityInfoWithEquipmentModule equipmentModule = new EntityInfoWithEquipmentModule(EntityType.PIG, integerAmountModule,itemWeightedModule);
        netherRaidModule.setReadyTime(10).setWaves(List.of(
                List.of(entityInfoModule),
                List.of(equipmentModule)
        )).setRewards(itemWeightedModule);

        addModule(netherRaidModule);
    }
}
