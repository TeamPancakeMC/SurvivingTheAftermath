package com.pancake.surviving_the_aftermath.api.module;

import com.pancake.surviving_the_aftermath.api.IIdentifier;
import com.pancake.surviving_the_aftermath.api.IJSONSerializable;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;

public interface IEntityInfoModule extends IIdentifier, IJSONSerializable, INBTSerializable<CompoundTag> {
    List<LazyOptional<Entity>> spawnEntity(ServerLevel level);
}
