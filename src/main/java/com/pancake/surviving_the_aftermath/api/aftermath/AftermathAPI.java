package com.pancake.surviving_the_aftermath.api.aftermath;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import com.pancake.surviving_the_aftermath.api.IAftermathFactory;
import com.pancake.surviving_the_aftermath.api.ITracker;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.api.module.IWeightedListModule;
import org.slf4j.Logger;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

public class AftermathAPI {
    private final Map<String, List<IAftermathModule>> AFTERMATH_MODULE_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends IAftermathModule>> AFTERMATH_MODULES_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends IWeightedListModule<?>>> WEIGHTED_LIST_MODULES_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends IAftermathFactory>> AFTERMATH_FACTORY_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends IAmountModule>> AMOUNT_MODULES_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends IEntityInfoModule>> ENTITY_INFO_MODULES_MAP = Maps.newHashMap();
    private final Map<String, Class<? extends ITracker>> TRACKERS = Maps.newHashMap();
    private static final AftermathAPI INSTANCE = new AftermathAPI();
    public static final Logger LOGGER = LogUtils.getLogger();
    public static AftermathAPI getInstance() {
        return INSTANCE;
    }
    private AftermathAPI() {}
    public void finishAftermathMap(Map<String, List<IAftermathModule>> map) {
        AFTERMATH_MODULE_MAP.putAll(map);
    }

    public Map<String, List<IAftermathModule>> getAftermathMap() {
        return AFTERMATH_MODULE_MAP;
    }

    public Optional<IAftermathModule> getRandomAftermathModule(String identifier) {
        List<IAftermathModule> aftermathModules = AFTERMATH_MODULE_MAP.get(identifier);
        return Optional.ofNullable(aftermathModules.get(new Random().nextInt(aftermathModules.size())));
    }

    public List<IAftermathModule> getAftermathModules(String identifier) {
        return AFTERMATH_MODULE_MAP.get(identifier);
    }


    public void registerTracker(String identifier, Class<? extends ITracker> tracker) {
        TRACKERS.put(identifier, tracker);
    }

    public List<ITracker> getTracker(UUID uuid, String... identifiers) {
        return Arrays.stream(identifiers)
                .map(identifier -> getObjectInstance(TRACKERS, identifier))
                .filter(Objects::nonNull)
                .map(tracker -> tracker.setUUID(uuid))
                .toList();
    }



    public void registerAmountModule(String identifier, Class<? extends IAmountModule> amountModule) {
        AMOUNT_MODULES_MAP.put(identifier, amountModule);
    }

    public IAmountModule getAmountModule(String identifier) {
        return getObjectInstance(AMOUNT_MODULES_MAP, identifier);
    }

    public void registerEntityInfoModule(String identifier, Class<? extends IEntityInfoModule> entityInfoModule) {
        ENTITY_INFO_MODULES_MAP.put(identifier, entityInfoModule);
    }

    public IEntityInfoModule getEntityInfoModule(String identifier) {
        return getObjectInstance(ENTITY_INFO_MODULES_MAP, identifier);
    }

    public void registerAftermathModule(String identifier, Class<? extends IAftermathModule> aftermathModule) {
        AFTERMATH_MODULES_MAP.put(identifier, aftermathModule);
    }

    public IAftermathModule getAftermathModule(String identifier) {
        return getObjectInstance(AFTERMATH_MODULES_MAP, identifier);
    }

    public void registerWeightedListModule(String identifier, Class<? extends IWeightedListModule<?>> weightedListModule) {
        WEIGHTED_LIST_MODULES_MAP.put(identifier, weightedListModule);
    }

    public IWeightedListModule<?> getWeightedListModule(String identifier) {
        return getObjectInstance(WEIGHTED_LIST_MODULES_MAP, identifier);
    }

    public void registerAftermathFactory(String identifier, Class<? extends IAftermathFactory> aftermathFactory) {
        AFTERMATH_FACTORY_MAP.put(identifier, aftermathFactory);
    }

    public IAftermathFactory getAftermathFactory(String identifier) {
        return getObjectInstance(AFTERMATH_FACTORY_MAP, identifier);
    }

    private <T> T getObjectInstance(Map<String, Class<? extends T>> objectMap, String identifier) {
        if (!objectMap.containsKey(identifier)) {
            LOGGER.error("AftermathAPI Failed to find object with identifier {}", identifier);
        }
        try {
            Class<? extends T> objectClass = objectMap.get(identifier);
            return objectClass.getDeclaredConstructor().newInstance();
        } catch (InstantiationException e) {
            LOGGER.error("AftermathAPI Failed to instantiate object with identifier {} error : {}", identifier, e);
        } catch (IllegalAccessException e) {
            LOGGER.error("AftermathAPI Failed to access object with identifier {} error : {}", identifier , e);
        } catch (NoSuchMethodException e) {
            LOGGER.error("AftermathAPI Failed to find constructor of object with identifier {} error : {}", identifier, e);
        } catch (InvocationTargetException e) {
            LOGGER.error("AftermathAPI Failed to invoke constructor of object with identifier {} error : {}", identifier, e);
        }catch (NullPointerException e){
            LOGGER.error("AftermathAPI Failed to find object with identifier :  {}", identifier);
        }
        return null;
    }





}
