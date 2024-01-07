package com.pancake.surviving_the_aftermath.common.data.pack;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.Constant;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import org.jetbrains.annotations.NotNull;
import java.util.Map;

public class AftermathModuleLoader extends SimpleJsonResourceReloadListener {
    public static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    public static final Multimap<ResourceLocation, IAftermathModule> AFTERMATH_MODULE_MAP = ArrayListMultimap.create();

    public AftermathModuleLoader() {
        super(GSON, "aftermath");
    }
    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, @NotNull ResourceManager manager, @NotNull ProfilerFiller filler) {
        AFTERMATH_MODULE_MAP.clear();
        jsonElementMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();

            IAftermathModule.CODEC.get()
                    .parse(JsonOps.INSTANCE, asJsonObject)
                    .resultOrPartial(SurvivingTheAftermath.LOGGER::error)
                    .ifPresent(aftermathModule -> {
                        AFTERMATH_MODULE_MAP.put(resourceLocation, aftermathModule);
                    });
        });
    }
}