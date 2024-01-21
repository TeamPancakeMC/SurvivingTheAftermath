package com.pancake.surviving_the_aftermath.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AftermathConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.BooleanValue enableMobBattleTrackerHighlight;
    public static ForgeConfigSpec.BooleanValue enableMobBattleTrackerRestrictedRange;


    static {
        BUILDER.comment("MobBattleTracker Config");

        enableMobBattleTrackerHighlight = BUILDER.comment("Enable MobBattleTracker Highlight")
                .define("enableMobBattleTrackerHighlight", true);

        enableMobBattleTrackerRestrictedRange = BUILDER.comment("Enable MobBattleTracker RestrictedRange")
                .define("enableMobBattleTrackerRestrictedRange", true);
    }

    static {
        SPEC = BUILDER.build();
    }
}
