package com.pancake.surviving_the_aftermath.api.base;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.module.condition.LevelConditionModule;
import com.pancake.surviving_the_aftermath.common.module.condition.PlayerConditionModule;
import com.pancake.surviving_the_aftermath.common.module.condition.StructureConditionModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;


public abstract class BaseAftermathModule implements IAftermathModule {
    protected String name;
    protected ItemWeightedModule rewards;
    protected List<IConditionModule> conditions = Lists.newArrayList();

    public BaseAftermathModule(String name,ItemWeightedModule rewards, List<IConditionModule> conditions) {
        this.name = name;
        this.rewards = rewards;
        this.conditions = conditions;
        findStructureStartingPoint();
    }

    public BaseAftermathModule() {
    }

    @Override
    public boolean isCreate(Level level, BlockPos pos, @Nullable Player player) {
        if (conditions.isEmpty()) return true;

        for (IConditionModule condition : conditions) {
            if (condition instanceof LevelConditionModule levelConditionModule) {
                return levelConditionModule.checkCondition(level,pos);
            }
            if (condition instanceof PlayerConditionModule playerConditionModule && player != null) {
                return playerConditionModule.checkCondition(player);
            }
        }
        return true;

    }

    private void findStructureStartingPoint() {
        List<IConditionModule> mutableConditions = Lists.newArrayList(conditions);

        Optional<IConditionModule> structureConditionModules = mutableConditions.stream()
                .filter(condition -> condition instanceof StructureConditionModule)
                .findAny();

        if (structureConditionModules.isPresent()) {
            mutableConditions.removeIf(condition -> condition instanceof StructureConditionModule);
            mutableConditions.add(structureConditionModules.get());
        }

        conditions = ImmutableList.copyOf(mutableConditions);
    }

    public ItemWeightedModule getRewards() {
        return rewards;
    }

    public List<IConditionModule> getConditions() {
        return conditions;
    }

    @Override
    public String getModuleName() {
        return name;
    }

    public BaseAftermathModule setName(String name) {
        this.name = name;
        return this;
    }

    public BaseAftermathModule setRewards(ItemWeightedModule rewards) {
        this.rewards = rewards;
        return this;
    }

    public BaseAftermathModule setConditions(List<IConditionModule> conditions) {
        this.conditions = conditions;
        return this;
    }

    public static class Builder<T extends IAftermathModule> {
        protected T module;
        protected List<IConditionModule> conditions = Lists.newArrayList();
        protected  ItemWeightedModule rewards;
        protected String name;
        public Builder(String name) {
            this.name = name;
        }

        public Builder<T> rewards(ItemWeightedModule Rewards) {
            this.rewards = Rewards;
            return this;
        }
        public Builder<T> addCondition(IConditionModule module){
            this.conditions.add(module);
            return this;
        }

        public T build() {
            return module;
        }
    }
}