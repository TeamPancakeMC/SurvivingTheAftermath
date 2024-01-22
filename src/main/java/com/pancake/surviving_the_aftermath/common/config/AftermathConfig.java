package com.pancake.surviving_the_aftermath.common.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class AftermathConfig {
    public static final ForgeConfigSpec SPEC;
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();

    public static ForgeConfigSpec.BooleanValue enableMobBattleTrackerHighlight;
    public static ForgeConfigSpec.BooleanValue enableMobBattleTrackerRestrictedRange;
    public static ForgeConfigSpec.BooleanValue enableSpawnPointStructure;


    static {
        BUILDER.comment("Config");

        enableMobBattleTrackerHighlight = BUILDER.comment("Enable MobBattleTracker Highlight")
                .define("enableMobBattleTrackerHighlight", true);

        enableMobBattleTrackerRestrictedRange = BUILDER.comment("Enable MobBattleTracker RestrictedRange")
                .define("enableMobBattleTrackerRestrictedRange", true);

        enableSpawnPointStructure = BUILDER.comment("Enable SpawnPoint Structure")
                .define("enableSpawnPointStructure", true);
    }

    static {
        SPEC = BUILDER.build();
    }
}
