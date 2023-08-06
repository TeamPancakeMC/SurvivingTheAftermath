package mod.surviving_the_aftermath.data;

import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;

import java.util.List;

public class RaidInfo {
    private final SimpleWeightedRandomList<Item> rewards;
    private final List<List<WaveEntry>> waves;

    public RaidInfo(SimpleWeightedRandomList<Item> rewards, List<List<WaveEntry>> waves) {
        this.rewards = rewards;
        this.waves = waves;
    }

    public SimpleWeightedRandomList<Item> getRewards() {
        return rewards;
    }

    public List<List<WaveEntry>> getWaves() {
        return waves;
    }

}
