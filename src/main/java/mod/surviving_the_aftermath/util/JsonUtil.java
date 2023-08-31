package mod.surviving_the_aftermath.util;

import com.google.gson.*;
import mod.surviving_the_aftermath.data.RaidEnemyInfo;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class JsonUtil {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(RaidEnemyInfo.WaveEntry.class, new WaveEntryDeserializer())
            .registerTypeAdapter(SimpleWeightedRandomList.class, new SimpleWeightedRandomListDeserializer())
            .setPrettyPrinting()
            .create();

    public static class WaveEntryDeserializer implements JsonDeserializer<RaidEnemyInfo.WaveEntry> {
        @Override
        public RaidEnemyInfo.WaveEntry deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject asJsonObject = json.getAsJsonObject();
            String type = asJsonObject.get("type").getAsString();
            int min = asJsonObject.get("min").getAsInt();
            int max = asJsonObject.get("max").getAsInt();
            JsonArray asJsonArray = asJsonObject.get("gear").getAsJsonArray();
            List<Item> gear = new ArrayList<>();
            for (JsonElement jsonElement : asJsonArray) {
                Item value = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(jsonElement.getAsString()));
                if (value == null) throw new JsonParseException("Unknown item: " + jsonElement.getAsString());
                gear.add(value);
            }
            EntityType<?> value = ForgeRegistries.ENTITY_TYPES.getValue(ResourceLocation.tryParse(type));
            if (value == null) throw new JsonParseException("Unknown entity type: " + type);
            return new RaidEnemyInfo.WaveEntry(value, min, max, gear);
        }
    }

    public static class SimpleWeightedRandomListDeserializer implements JsonDeserializer<SimpleWeightedRandomList<Item>> {
        @Override
        public SimpleWeightedRandomList<Item> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonArray asJsonArray = json.getAsJsonArray();
            SimpleWeightedRandomList.Builder<Item> builder = SimpleWeightedRandomList.builder();
            for (JsonElement jsonElement : asJsonArray) {
                JsonObject asJsonObject = jsonElement.getAsJsonObject();
                Item item = ForgeRegistries.ITEMS.getValue(ResourceLocation.tryParse(asJsonObject.get("id").getAsString()));
                int weight = asJsonObject.get("weighted").getAsInt();
                if (item == null) throw new JsonParseException("Unknown item: " + asJsonObject.get("item").getAsString());
                builder.add(item, weight);
            }
            return builder.build();
        }
    }
}