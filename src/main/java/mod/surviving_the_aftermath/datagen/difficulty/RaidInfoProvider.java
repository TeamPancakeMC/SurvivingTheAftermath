package mod.surviving_the_aftermath.datagen.difficulty;

import mod.surviving_the_aftermath.data.RaidInfo;
import mod.surviving_the_aftermath.data.WaveEntry;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.item.Item;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

public abstract class RaidInfoProvider implements DataProvider {
    @Override
    public CompletableFuture<?> run(CachedOutput pOutput) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
