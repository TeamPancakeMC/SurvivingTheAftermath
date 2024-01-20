package com.pancake.surviving_the_aftermath.common.module.predicate;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.pancake.surviving_the_aftermath.api.module.IPredicateModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.LivingEntity;


public class NBTPredicate implements IPredicateModule {
    public static final String IDENTIFIER = "nbt";
    public static final Codec<NBTPredicate> CODEC = CompoundTag.CODEC.xmap(NBTPredicate::new, NBTPredicate::getNBT);

    public CompoundTag nbt = new CompoundTag();

    public NBTPredicate(CompoundTag nbt) {
        this.nbt = nbt;
    }
    public NBTPredicate() {
    }

    public<T> NBTPredicate add(String key, T value) {
        if (value instanceof Integer integer) {
            nbt.putInt(key, integer);
        } else if (value instanceof String string) {
            nbt.putString(key, string);
        } else if (value instanceof Boolean bool) {
            nbt.putBoolean(key, bool);
        } else if (value instanceof Float float1) {
            nbt.putFloat(key, float1);
        } else if (value instanceof Double double1) {
            nbt.putDouble(key, double1);
        } else if (value instanceof Byte byte1) {
            nbt.putByte(key, byte1);
        } else if (value instanceof Short short1) {
            nbt.putShort(key, short1);
        } else if (value instanceof Long long1) {
            nbt.putLong(key, long1);
        } else if (value instanceof CompoundTag tag) {
            nbt.put(key, tag);
        } else if (value instanceof int[] ints) {
            nbt.putIntArray(key, ints);
        } else if (value instanceof long[] longs) {
            nbt.putLongArray(key, longs);
        } else if (value instanceof byte[] bytes) {
            nbt.putByteArray(key, bytes);
        }
        return this;
    }

    @Override
    public Codec<? extends IPredicateModule> codec() {
        return CODEC;
    }

    public CompoundTag getNBT() {
        return nbt;
    }

    @Override
    public IPredicateModule type() {
        return ModAftermathModule.NBT_PREDICATE.get();
    }

    @Override
    public void apply(LivingEntity livingEntity) {
        livingEntity.readAdditionalSaveData(nbt);
    }

    public static class Builder {
        private NBTPredicate predicate;
        public<T> Builder add(String key, T value) {
            predicate.add(key,value);
            return this;
        }
        public NBTPredicate build(){
            return predicate;
        }
    }
}
