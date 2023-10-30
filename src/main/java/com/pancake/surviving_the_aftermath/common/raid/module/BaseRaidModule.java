package com.pancake.surviving_the_aftermath.common.raid.module;

import com.google.common.collect.Lists;
import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaidModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.List;

public class BaseRaidModule extends BaseAftermathModule implements IRaidModule {
    private List<List<IEntityInfoModule>> waves = Lists.newArrayList();

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        ListTag waves = new ListTag();
        for (List<IEntityInfoModule> wave : this.waves) {
            ListTag listTag = new ListTag();
            for (IEntityInfoModule entityInfoModule : wave) {
                listTag.add(entityInfoModule.serializeNBT());
            }
            waves.add(listTag);
        }
        compoundTag.put(Constant.WAVES, waves);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        super.deserializeNBT(compoundTag);
        ListTag waves = compoundTag.getList(Constant.WAVES, 9);
        for (Tag wave : waves) {
            ListTag listTag = (ListTag) wave;
            ArrayList<IEntityInfoModule> modules = Lists.newArrayList();
            for (Tag tag: listTag) {
                CompoundTag compoundTag1 = (CompoundTag) tag;
                IEntityInfoModule entityInfoModule = AftermathAPI.getInstance().getEntityInfoModule(compoundTag1.getString(Constant.IDENTIFIER));
                entityInfoModule.deserializeNBT(compoundTag1);
                modules.add(entityInfoModule);
            }
            this.waves.add(modules);
        }
    }

    @Override
    public void deserializeJson(JsonElement jsonElement) {
        super.deserializeJson(jsonElement);
        jsonElement.getAsJsonObject().get(Constant.WAVES).getAsJsonArray().forEach(jsonElement1 -> {
            ArrayList<IEntityInfoModule> modules = Lists.newArrayList();
            jsonElement1.getAsJsonArray().forEach(jsonElement2 -> {
                IEntityInfoModule entityInfoModule = AftermathAPI.getInstance().getEntityInfoModule(GsonHelper.getAsString(jsonElement2.getAsJsonObject(), Constant.IDENTIFIER));
                entityInfoModule.deserializeJson(jsonElement2);
                modules.add(entityInfoModule);
            });
            this.waves.add(modules);
        });
    }

    @Override
    public String getUniqueIdentifier() {
        return NetherRaid.IDENTIFIER;
    }

    @Override
    public List<List<IEntityInfoModule>> getWaves() {
        return waves;
    }
}
