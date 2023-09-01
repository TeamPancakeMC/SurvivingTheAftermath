package mod.surviving_the_aftermath.data;

import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.List;

public record RaidEnemyInfo (SimpleWeightedRandomList<Item> rewards, List<List<WaveEntry>> waves){
        public record WaveEntry(EntityType<?> type, int min, int max, List<Item> gear) {
        }
}

