package mod.surviving_the_aftermath.data;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

import java.util.List;

public final class WaveEntry {
	private final EntityType<?> type;
    private final int min;
    private final int max;
    private final List<Item> gear;

    public WaveEntry(EntityType<?> type, int min, int max, List<Item> gear) {
        this.type = type;
        this.min = min;
        this.max = max;
        this.gear = gear;
    }

	public EntityType<?> getType() {
		return type;
	}

	public int getMin() {
		return min;
	}

	public int getMax() {
		return max;
	}

	public List<Item> getGear() {
		return gear;
	}
}