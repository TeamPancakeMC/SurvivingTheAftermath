package com.pancake.surviving_the_aftermath.common.data.datagen;

import com.google.common.collect.Lists;
import com.google.common.hash.Hashing;
import com.google.common.hash.HashingOutputStream;
import com.google.gson.JsonElement;
import com.google.gson.stream.JsonWriter;
import com.mojang.serialization.JsonOps;
import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.api.base.BaseAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.common.module.amount.IntegerAmountModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoModule;
import com.pancake.surviving_the_aftermath.common.module.weighted.ItemWeightedModule;
import com.pancake.surviving_the_aftermath.common.raid.module.NetherRaidModule;
import net.minecraft.Util;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public abstract class AftermathModuleProviders<T extends IAftermathModule> implements DataProvider {
    private final PackOutput output;
    private final String modId;
    private final List<T> modules = Lists.newArrayList();


    public AftermathModuleProviders(PackOutput output, String modId) {
        this.output = output;
        this.modId = modId;
    }


    @Override
    @NotNull
    public CompletableFuture<?> run(@NotNull CachedOutput output) {
        addModules();
        return CompletableFuture.runAsync(() -> modules.forEach(module -> {
            IAftermathModule.CODEC.get()
                    .encodeStart(JsonOps.INSTANCE, module)
                    .resultOrPartial(SurvivingTheAftermath.LOGGER::error)
                    .ifPresent(jsonElement -> {
                        Save(output, module, jsonElement);
                    });
        }), Util.backgroundExecutor());
//        IAftermathModule.CODEC.get().encodeStart(JsonOps.INSTANCE, netherRaidModule)
//                .resultOrPartial(SurvivingTheAftermath.LOGGER::error)
//                .ifPresent(jsonElement -> {
//                    Save(output, netherRaidModule, jsonElement);
//                });


//        return CompletableFuture.allOf();
    }

    private void Save(CachedOutput output, T module, JsonElement jsonElement) {
        try {
            ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
            HashingOutputStream hashingoutputstream = new HashingOutputStream(Hashing.sha1(), bytearrayoutputstream);

            try (JsonWriter jsonwriter = new JsonWriter(new OutputStreamWriter(hashingoutputstream, StandardCharsets.UTF_8))) {
                jsonwriter.setSerializeNulls(false);
                jsonwriter.setIndent("  ");
                GsonHelper.writeValue(jsonwriter, jsonElement, KEY_COMPARATOR);
            }
            Path path = this.output.getOutputFolder(PackOutput.Target.DATA_PACK)
                    .resolve(this.modId).resolve("aftermath").resolve(module.getJsonName().toLowerCase() + ".json");
            output.writeIfNeeded(path, bytearrayoutputstream.toByteArray(), hashingoutputstream.hash());
        } catch (IOException ioexception) {
            LOGGER.error("Failed to save file to {}", output, ioexception);
        }
    }


    public void addModule(T module) {
        modules.add(module);
    }
    public abstract void addModules();
    @Override
    @NotNull
    public String getName() {
        return "AftermathModuleProviders";
    }
}