package com.pancake.surviving_the_aftermath.api;

import com.google.common.collect.Maps;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class AftermathAPI {
    private final Map<String, List<IAftermathModule>> AFTERMATH_MODULE_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends IBattleTracker>> BATTLE_TRACKER_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends IAftermathFactory>> AFTERMATH_FACTORY_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends IAftermathModule>> AFTERMATH_MODULES_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends IWeightedListModule<?>>> WEIGHTED_LIST_MODULES_MAP = Maps.newHashMap();
    private static final AftermathAPI INSTANCE = new AftermathAPI();
    public static AftermathAPI getInstance() {
        return INSTANCE;
    }
    private AftermathAPI() {
    }
    public void finishAftermathMap(Map<String, List<IAftermathModule>> map) {
        AFTERMATH_MODULE_MAP.putAll(map);
    }

    public Map<String, List<IAftermathModule>> getAftermathMap() {
        return AFTERMATH_MODULE_MAP;
    }

    public IAftermathModule getAftermathModule(String identifier) {
        List<IAftermathModule> iAftermathModules = AFTERMATH_MODULE_MAP.get(identifier);
        return iAftermathModules.get(new Random().nextInt(iAftermathModules.size()));
    }

    public void registerBattleTracker(String identifier, Class<? extends IBattleTracker> battleTracker) {
        BATTLE_TRACKER_MAP.put(identifier, battleTracker);
    }

    public IBattleTracker getBattleTracker(String identifier) {
        return getObjectInstance(BATTLE_TRACKER_MAP, identifier);
    }


    public void registerAftermathFactory(String identifier, Class<? extends IAftermathFactory> aftermathFactory) {
        AFTERMATH_FACTORY_MAP.put(identifier, aftermathFactory);
    }

    public IAftermathFactory getAftermathFactory(String identifier) {
        return getObjectInstance(AFTERMATH_FACTORY_MAP, identifier);
    }
    public void registerWeightedListModule(String identifier, Class<? extends IWeightedListModule<?>> weightedListModule) {
        WEIGHTED_LIST_MODULES_MAP.put(identifier, weightedListModule);
    }
    public IWeightedListModule<?> getWeightedListModule(String identifier) {
        return getObjectInstance(WEIGHTED_LIST_MODULES_MAP, identifier);
    }

    public void registerAftermathModule(String identifier, Class<? extends IAftermathModule> aftermathModule) {
        AFTERMATH_MODULES_MAP.put(identifier, aftermathModule);
    }

    public IAftermathModule getAftermathModuleClass(String identifier) {
        return getObjectInstance(AFTERMATH_MODULES_MAP, identifier);

    }

    private <T> T getObjectInstance(Map<String, Class<? extends T>> objectMap, String identifier) {
        if (!objectMap.containsKey(identifier)) {
            throw new IllegalArgumentException("Object " + identifier + " is not registered!");
        }
        try {
            Class<? extends T> objectClass = objectMap.get(identifier);
            return objectClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e) {
            throw new RuntimeException("Failed to create object " + identifier, e);
        }
    }
}
