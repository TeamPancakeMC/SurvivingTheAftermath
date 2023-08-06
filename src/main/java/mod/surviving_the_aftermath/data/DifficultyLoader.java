package mod.surviving_the_aftermath.data;


import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.surviving_the_aftermath.util.JsonUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;
import org.jetbrains.annotations.NotNull;
import com.google.common.collect.Maps;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class DifficultyLoader extends SimpleJsonResourceReloadListener {
    private static Map<String, List<RaidInfo>> RAID_MAP = Maps.newHashMap();
    private static Map<String,RaidInfo> RAID_INFO_MAP = Maps.newHashMap();
    public DifficultyLoader(String directory) {
        super(JsonUtil.GSON, directory);
    }

    @Override
    protected void apply(@NotNull Map<ResourceLocation, JsonElement> jsonElementMap, @NotNull ResourceManager resourceManager, @NotNull ProfilerFiller filler) {
        deserialize(jsonElementMap);
    }

    protected static void deserialize(@NotNull Map<ResourceLocation, JsonElement> jsonElementMap) {
        jsonElementMap.forEach((resourceLocation, jsonElement) -> {
            System.out.println("resourceLocation" + resourceLocation);
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String id = asJsonObject.get("id").getAsString();
            SimpleWeightedRandomList<Item> rewards = JsonUtil.GSON.fromJson(asJsonObject.get("rewards"), SimpleWeightedRandomList.class);
            List<List<WaveEntry>> waves = asJsonObject.get("waves").getAsJsonArray().asList().stream()
                    .map(JsonElement::getAsJsonArray)
                    .map(jsonArray -> {
                        List<WaveEntry> list = new ArrayList<>();
                        jsonArray.forEach(element -> list.add(JsonUtil.GSON.fromJson(element, WaveEntry.class)));
                        return list;
                    })
                    .collect(Collectors.toList());
            RaidInfo info = new RaidInfo(rewards, waves);
            if (RAID_MAP.containsKey(id)) {
                RAID_MAP.get(id).add(info);
            } else {
                RAID_MAP.put(id, new ArrayList<>(List.of(info)));
            }
            RAID_INFO_MAP.put(resourceLocation.toString(), info);
        });
    }

    public Map<String, List<RaidInfo>> getRaidMap() {
        return RAID_MAP;
    }
    public Map<String, RaidInfo> getRaidInfoMap() {
        return RAID_INFO_MAP;
    }
    public static RaidInfo getRaidInfo(String key) {
        return RAID_INFO_MAP.get(key);
    }
    public static String getKeyFoRaidInfo(RaidInfo info) {
        return RAID_INFO_MAP.entrySet().stream()
                .filter(entry -> entry.getValue().equals(info))
                .map(Map.Entry::getKey)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("RaidInfo not found"));
    }
}
