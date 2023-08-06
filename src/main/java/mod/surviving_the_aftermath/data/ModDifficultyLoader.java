package mod.surviving_the_aftermath.data;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ModDifficultyLoader {
    public static class Hard extends SimpleJsonResourceReloadListener {
        public static Map<String, List<RaidInfo>> RAID_MAP = Maps.newHashMap();
        public static Map<String, RaidInfo> RAID_INFO_MAP = Maps.newHashMap();
        public Hard() {
            super(JsonUtil.GSON, "raid/hard");
        }

        @Override
        protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
            difficultyDeserialize(pObject, RAID_MAP, RAID_INFO_MAP);
        }
    }
    public static class Normal extends SimpleJsonResourceReloadListener {
        public static Map<String, List<RaidInfo>> RAID_MAP = Maps.newHashMap();
        public static Map<String, RaidInfo> RAID_INFO_MAP = Maps.newHashMap();
        public Normal() {
            super(JsonUtil.GSON, "raid/normal");
        }
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
            difficultyDeserialize(pObject, RAID_MAP, RAID_INFO_MAP);
        }

    }

    public static class Easy extends SimpleJsonResourceReloadListener {
        public static Map<String, List<RaidInfo>> RAID_MAP = Maps.newHashMap();
        public static Map<String, RaidInfo> RAID_INFO_MAP = Maps.newHashMap();
        public Easy() {
            super(JsonUtil.GSON, "raid/easy");
        }
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
            difficultyDeserialize(pObject, RAID_MAP, RAID_INFO_MAP);
        }

    }

    public static class Peaceful extends SimpleJsonResourceReloadListener {
        public static Map<String, List<RaidInfo>> RAID_MAP = Maps.newHashMap();
        public static Map<String, RaidInfo> RAID_INFO_MAP = Maps.newHashMap();
        public Peaceful() {
            super(JsonUtil.GSON, "raid/peaceful");
        }
        @Override
        protected void apply(Map<ResourceLocation, JsonElement> pObject, ResourceManager pResourceManager, ProfilerFiller pProfiler) {
            difficultyDeserialize(pObject, RAID_MAP, RAID_INFO_MAP);
        }

    }

    private static void difficultyDeserialize(@NotNull Map<ResourceLocation, JsonElement> jsonElementMap, Map<String, List<RaidInfo>> raidMap, Map<String, RaidInfo> raidInfoMap) {
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
            if (raidMap.containsKey(id)) {
                raidMap.get(id).add(info);
            } else {
                raidMap.put(id, new ArrayList<>(List.of(info)));
            }
            raidInfoMap.put(resourceLocation.toString(), info);
        });
    }

    public static Map<String, List<RaidInfo>> getRaidMap(int difficulty) {
        return switch (difficulty) {
            case 0 -> Peaceful.RAID_MAP;
            case 1 -> Easy.RAID_MAP;
            case 2 -> Normal.RAID_MAP;
            case 3 -> Hard.RAID_MAP;
            default -> throw new IllegalStateException("Unexpected value: " + difficulty);
        };
    }

    public static Map<String, RaidInfo> getRaidInfoMap(int difficulty) {
        return switch (difficulty) {
            case 0 -> Peaceful.RAID_INFO_MAP;
            case 1 -> Easy.RAID_INFO_MAP;
            case 2 -> Normal.RAID_INFO_MAP;
            case 3 -> Hard.RAID_INFO_MAP;
            default -> throw new IllegalStateException("Unexpected value: " + difficulty);
        };
    }


    public static String getRaidInfoString(RaidInfo raidInfo,int difficulty) {
        return switch (difficulty) {
            case 0 -> Peaceful.RAID_INFO_MAP.entrySet().stream().filter(entry -> entry.getValue().equals(raidInfo)).findFirst().get().getKey();
            case 1 -> Easy.RAID_INFO_MAP.entrySet().stream().filter(entry -> entry.getValue().equals(raidInfo)).findFirst().get().getKey();
            case 2 -> Normal.RAID_INFO_MAP.entrySet().stream().filter(entry -> entry.getValue().equals(raidInfo)).findFirst().get().getKey();
            case 3 -> Hard.RAID_INFO_MAP.entrySet().stream().filter(entry -> entry.getValue().equals(raidInfo)).findFirst().get().getKey();
            default -> throw new IllegalStateException("Unexpected value: " + difficulty);
        };
    }


    public static RaidInfo getRaidInfo(String id,int difficulty) {
        return switch (difficulty) {
            case 0 -> Peaceful.RAID_INFO_MAP.get(id);
            case 1 -> Easy.RAID_INFO_MAP.get(id);
            case 2 -> Normal.RAID_INFO_MAP.get(id);
            case 3 -> Hard.RAID_INFO_MAP.get(id);
            default -> throw new IllegalStateException("Unexpected value: " + difficulty);
        };
    }

    public static List<RaidInfo> getRaidInfoList(String id,int difficulty) {
        return switch (difficulty) {
            case 0 -> Peaceful.RAID_MAP.get(id);
            case 1 -> Easy.RAID_MAP.get(id);
            case 2 -> Normal.RAID_MAP.get(id);
            case 3 -> Hard.RAID_MAP.get(id);
            default -> throw new IllegalStateException("Unexpected value: " + difficulty);
        };
    }




}