package com.pancake.surviving_the_aftermath.api;

import com.mojang.serialization.Codec;

public interface ICodec<T> {
    Codec<? extends T> codec();
    T type();
}
