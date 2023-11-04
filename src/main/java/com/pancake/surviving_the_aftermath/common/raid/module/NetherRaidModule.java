package com.pancake.surviving_the_aftermath.common.raid.module;

import com.google.gson.JsonElement;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.common.raid.NetherRaid;
import net.minecraft.nbt.CompoundTag;

public class NetherRaidModule extends BaseRaidModule {
    protected int readyTime;
    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = super.serializeNBT();
        compoundTag.putInt(Constant.READY_TIME,readyTime);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag compoundTag) {
        super.deserializeNBT(compoundTag);
        this.readyTime = compoundTag.getInt(Constant.READY_TIME);
    }

    @Override
    public void deserializeJson(JsonElement jsonElement) {
        super.deserializeJson(jsonElement);
        this.readyTime = jsonElement.getAsJsonObject().get(Constant.READY_TIME).getAsInt();
    }

    @Override
    public JsonElement serializeJson() {
        JsonElement jsonElement = super.serializeJson();
        jsonElement.getAsJsonObject().addProperty(Constant.READY_TIME,readyTime);
        return jsonElement;
    }

    @Override
    public String getUniqueIdentifier() {
        return NetherRaid.IDENTIFIER;
    }

    public int getReadyTime() {
        return readyTime;
    }

    protected void setReadyTime(int readyTime) {
        this.readyTime = readyTime;
    }


    public static class Builder extends BaseRaidModule.Builder<NetherRaidModule>{
        private int readyTime;

        public Builder(String jsonName) {
            super(new NetherRaidModule(), jsonName);
        }

        public Builder setReadyTime(int readyTime) {
            this.readyTime = readyTime;
            return this;
        }

        @Override
        public NetherRaidModule build() {
            NetherRaidModule build = super.build();
            build.setReadyTime(this.readyTime);
            return build;
        }
    }
}
