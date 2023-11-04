package com.pancake.surviving_the_aftermath.common.raid.module;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.impl.weighted.ItemWeightedListModule;
import com.pancake.surviving_the_aftermath.common.raid.api.IRaidModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.util.GsonHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public abstract class BaseRaidModule extends BaseAftermathModule implements IRaidModule {
    protected List<List<IEntityInfoModule>> waves = Lists.newArrayList();

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
    public JsonElement serializeJson() {
        JsonElement jsonElement = super.serializeJson();
        JsonArray jsonArray = new JsonArray();
        for (List<IEntityInfoModule> wave : waves) {
            JsonArray jsonArray1 = new JsonArray();
            for (IEntityInfoModule entityInfoModule : wave) {
                jsonArray1.add(entityInfoModule.serializeJson());
            }
            jsonArray.add(jsonArray1);
        }
        jsonElement.getAsJsonObject().add(Constant.WAVES, jsonArray);
        return jsonElement;
    }

    @Override
    public List<List<IEntityInfoModule>> getWaves() {
        return waves;
    }

    protected void setWaves(List<List<IEntityInfoModule>> waves) {
        this.waves = waves;
    }

    @Override
    public abstract String getUniqueIdentifier();

    public static class Builder<T extends BaseAftermathModule>  extends BaseAftermathModule.Builder<T>{
        protected List<List<IEntityInfoModule>> waves = Lists.newArrayList();

        public Builder<T> addWave(Supplier<List<IEntityInfoModule>> wave) {
            this.waves.add(wave.get());
            return this;
        }

        public Builder<T> addWaves(Supplier<List<List<IEntityInfoModule>>> waves) {
            this.waves = waves.get();
            return this;
        }

        @Override
        public T build() {
            T module = super.build();
            if (module instanceof BaseRaidModule baseRaidModule) {
                baseRaidModule.setWaves(waves);
            }
            return module;
        }
    }
}
