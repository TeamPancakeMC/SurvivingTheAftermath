package com.pancake.surviving_the_aftermath.api.module.impl.condition;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.capability.StageDataCap;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public abstract class StageConditionModule implements IConditionModule {
    protected String stageName;

    public StageConditionModule(String stageName) {
        this.stageName = stageName;
    }

    public boolean checkCondition(Level level){
        return false;
    }

    public boolean checkCondition(Player player){
        return false;
    }


    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("stageName", stageName);
        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.stageName = nbt.getString("stageName");
    }

    @Override
    public void deserializeJson(JsonElement jsonElement) {
        this.stageName = jsonElement.getAsString();
    }

    @Override
    public JsonElement serializeJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("stageName", stageName);
        return jsonObject;
    }

    public static class LevelStageConditionModule extends StageConditionModule {
        public static final String IDENTIFIER = Constant.LEVEL_STAGES;
        public LevelStageConditionModule(String stageName) {
            super(stageName);
        }

        @Override
        public boolean checkCondition(Level level) {
            StageDataCap stageDataCap = StageDataCap.get(level).orElse(null);
            return stageDataCap.getStageData().hasStage(stageName);
        }

        @Override
        public String getUniqueIdentifier() {
            return IDENTIFIER;
        }
    }

    public static class PlayerStageConditionModule extends StageConditionModule {
        public static final String IDENTIFIER = Constant.PLAYER_STAGES;
        public PlayerStageConditionModule(String stageName) {
            super(stageName);
        }

        @Override
        public boolean checkCondition(Player player) {
            StageDataCap stageDataCap = StageDataCap.get(player).orElse(null);
            return stageDataCap.getStageData().hasStage(stageName);
        }

        @Override
        public String getUniqueIdentifier() {
            return IDENTIFIER;
        }
    }


}
