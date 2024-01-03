package com.pancake.surviving_the_aftermath.api.stage;

import com.google.common.collect.Sets;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.IStageData;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;

public abstract class StageData implements IStageData {
    private final Set<String> unlockedStages = Sets.newHashSet();
    private final String identifier;

    public StageData(String identifier) {
        this.identifier = identifier;
    }
    @Override
    public Collection<String> getStages() {

        return Collections.unmodifiableCollection(this.unlockedStages);
    }

    @Override
    public boolean hasStage(String stage) {

        return this.unlockedStages.contains(stage.toLowerCase());
    }
    
    @Override
    public void addStage(String stage) {

        this.unlockedStages.add(stage.toLowerCase());
    }

    @Override
    public void removeStage(String stage) {

        this.unlockedStages.remove(stage.toLowerCase());
    }

    @Override
    public void clear() {

        this.unlockedStages.clear();
    }


    @Override
    public String toString () {

        return "StageData [unlockedStages=" + this.unlockedStages + "]";
    }

    @Override
    public String getUniqueIdentifier() {
        return identifier;
    }

    @Override
    public CompoundTag serializeNBT() {
        final CompoundTag tag = new CompoundTag();
        final ListTag list = new ListTag();
        for (final String stage : this.unlockedStages) {
            list.add(StringTag.valueOf(stage));
        }
        tag.put(identifier, list);
        return tag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        final ListTag list = nbt.getList(identifier, Tag.TAG_STRING);
        for (int tagIndex = 0; tagIndex < list.size(); tagIndex++) {
            this.addStage(list.getString(tagIndex));
        }
    }
}
