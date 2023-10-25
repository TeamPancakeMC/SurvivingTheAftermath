package com.pancake.surviving_the_aftermath.tracker;

import com.pancake.surviving_the_aftermath.api.ITracker;

public class PlayerBattleTracker implements ITracker {
    public static final String IDENTIFIER = "player_battle_tracker";
    @Override
    public String getUniqueIdentifier() {
        return IDENTIFIER;
    }
}
