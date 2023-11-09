package com.pancake.surviving_the_aftermath.common.data.pack;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.aftermath.AftermathAPI;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.util.AftermathEventUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;

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
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, @NotNull ResourceManager manager, @NotNull ProfilerFiller filler) {
        System.out.println("Loading aftermath modules");
        AFTERMATH_MODULE_MAP.clear();
        AftermathAPI instance = AftermathAPI.getInstance();
        jsonElementMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String identifier = GsonHelper.getAsString(asJsonObject, Constant.IDENTIFIER);
            IAftermathModule aftermathModule = instance.getAftermathModule(identifier);
            try {
                aftermathModule.deserializeJson(asJsonObject);
                aftermathModule.setJsonName(resourceLocation.getPath());
                if (!AFTERMATH_MODULE_MAP.containsKey(identifier)) {
                    AFTERMATH_MODULE_MAP.put(identifier, new ArrayList<>(List.of(aftermathModule)));
                }
                AFTERMATH_MODULE_MAP.get(identifier).add(aftermathModule);
            }catch (NullPointerException e) {
                SurvivingTheAftermath.LOGGER.error("Failed to deserialize aftermath module: " + aftermathModule.getUniqueIdentifier());
            }
        });


        instance.finishAftermathMap(AFTERMATH_MODULE_MAP);
    }
}