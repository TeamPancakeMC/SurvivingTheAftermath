package com.pancake.surviving_the_aftermath.common.raid.api;

import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;

import java.util.List;

public interface IRaidModule {
    List<List<IEntityInfoModule>> getWaves();
}
