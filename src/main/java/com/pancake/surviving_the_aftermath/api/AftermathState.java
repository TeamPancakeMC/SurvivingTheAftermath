package com.pancake.surviving_the_aftermath.api;

import com.mojang.serialization.Codec;

public enum AftermathState {
    START("start", 0),
    READY("ready", 1),
    ONGOING("ongoing", 2),
    VICTORY("victory", 3),
    LOSE("lose", 4),
    CELEBRATING("celebrating", 5),
    END("end", 6);
    public static final Codec<AftermathState> CODEC = Codec.STRING.xmap(AftermathState::valueOf, AftermathState::name);
    private final String name;
    private final int index;

    AftermathState(String name, int index) {
        this.name = name;
        this.index = index;
    }

}