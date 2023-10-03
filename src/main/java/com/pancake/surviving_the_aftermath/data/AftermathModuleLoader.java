package com.pancake.surviving_the_aftermath.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.api.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AftermathModuleLoader extends SimpleJsonResourceReloadListener {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    public final Map<String,List<IAftermathModule>> AFTERMATH_MODULE_MAP = Maps.newHashMap();

    public AftermathModuleLoader() {
        super(GSON, "aftermath");
    }
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager manager, ProfilerFiller filler) {
        AFTERMATH_MODULE_MAP.clear();
        AftermathAPI instance = AftermathAPI.getInstance();
        jsonElementMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String identifier = GsonHelper.getAsString(asJsonObject, "identifier");
            IAftermathModule aftermathModule = instance.getAftermathModuleClass(identifier);
            aftermathModule.deserialize(asJsonObject);
            if (!AFTERMATH_MODULE_MAP.containsKey(identifier)) {
                AFTERMATH_MODULE_MAP.put(identifier, new ArrayList<>(List.of(aftermathModule)));
            }
            AFTERMATH_MODULE_MAP.get(identifier).add(aftermathModule);

        });
        instance.finishAftermathMap(AFTERMATH_MODULE_MAP);
    }
}