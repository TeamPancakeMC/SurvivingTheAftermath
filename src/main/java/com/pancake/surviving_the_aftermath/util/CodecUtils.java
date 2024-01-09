package com.pancake.surviving_the_aftermath.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class CodecUtils {
    public static final Codec<UUID> UUID_CODEC = Codec.STRING
            .xmap(UUID::fromString, UUID::toString)
            .fieldOf("uuid")
            .codec();

    //T
    public static <T> Codec<Set<T>> setOf(Codec<T> codec) {
        return Codec.list(codec).xmap(HashSet::new, ArrayList::new);
    }
}
