package mod.surviving_the_aftermath.data;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import mod.surviving_the_aftermath.util.JsonUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.SimpleJsonResourceReloadListener;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModDifficultyLoader extends SimpleJsonResourceReloadListener {
    private final Map<String, List<RaidEnemyInfo>> raidMap = Maps.newHashMap();

    ModDifficultyLoader(String name) {
        super(JsonUtil.GSON, name);
    }

    @Override
    protected void apply(Map<ResourceLocation, JsonElement> jsonElementMap, ResourceManager p_10794_, ProfilerFiller p_10795_) {
        jsonElementMap.forEach((resourceLocation, jsonElement) -> {
            JsonObject asJsonObject = jsonElement.getAsJsonObject();
            String id = asJsonObject.get("id").getAsString();
            SimpleWeightedRandomList<Item> rewards = JsonUtil.GSON.fromJson(asJsonObject.get("rewards"), SimpleWeightedRandomList.class);
            List<List<RaidEnemyInfo.WaveEntry>> waves = asJsonObject.get("waves").getAsJsonArray().asList().stream()
                    .map(JsonElement::getAsJsonArray)
                    .map(jsonArray -> {
                        List<RaidEnemyInfo.WaveEntry> list = new ArrayList<>();
                        jsonArray.forEach(element -> list.add(JsonUtil.GSON.fromJson(element, RaidEnemyInfo.WaveEntry.class)));
                        return list;
                    })
                    .collect(Collectors.toList());
            RaidEnemyInfo info = new RaidEnemyInfo(rewards, waves);
            if (raidMap.containsKey(id)) {
                raidMap.get(id).add(info);
            } else {
                raidMap.put(id, new ArrayList<>(List.of(info)));
            }
        });
    }

    public Map<String, List<RaidEnemyInfo>> getRaidMap() {
        return raidMap;
    }

    public List<RaidEnemyInfo> getRaidInfo(String identifier) {
        return raidMap.get(identifier);
    }

    public RaidEnemyInfo getRaidInfo(String identifier, int index) {
        return raidMap.get(identifier).get(index);
    }
}