package com.pancake.surviving_the_aftermath.api.base;

import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.module.condition.LevelConditionModule;
import com.pancake.surviving_the_aftermath.common.module.condition.PlayerConditionModule;
import com.pancake.surviving_the_aftermath.common.module.condition.StructureConditionModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import net.minecraft.core.BlockPos;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

import java.util.List;
import java.util.Optional;

public abstract class BaseAftermathModule implements IAftermathModule {
    protected ItemWeightedModule rewards;
    protected List<IConditionModule> conditions;
    protected String jsonName;

    public BaseAftermathModule(ItemWeightedModule rewards, List<IConditionModule> conditions) {
        this.rewards = rewards;
        this.conditions = getFilterConditions(conditions);
    }

    public BaseAftermathModule() {
    }


    //过滤条件
    public List<IConditionModule> getFilterConditions(List<IConditionModule> conditions){
        Optional<IConditionModule> module = conditions.stream()
                .filter(condition -> condition instanceof StructureConditionModule)
                .findFirst();

        module.ifPresent(iConditionModule ->
                conditions.removeIf(condition -> condition instanceof StructureConditionModule && condition != iConditionModule));

        return conditions;
    }

    @Override
    public boolean isCreate(Level level, BlockPos pos, Player player) {
        conditions.forEach(condition -> {
            if(condition instanceof LevelConditionModule levelConditionModule){
                levelConditionModule.checkCondition(level,pos);
            }
            if (condition instanceof PlayerConditionModule playerConditionModule){
                playerConditionModule.checkCondition(player);
            }
        });
        return true;
    }

    public List<IConditionModule> getConditions() {
        return conditions;
    }

    public ItemWeightedModule getRewards() {
        return rewards;
    }
    public List<WeightedEntry.Wrapper<Item>> getRewardsList() {
        return rewards.getList();
    }

    public BaseAftermathModule setRewards(ItemWeightedModule rewards) {
        this.rewards = rewards;
        return this;
    }

    public BaseAftermathModule setConditions(List<IConditionModule> conditions) {
        this.conditions = conditions;
        return this;
    }

    @Override
    public String getJsonName() {
        return jsonName;
    }
    @Override
    public void setJsonName(String jsonName) {
        this.jsonName = jsonName;
    }
}