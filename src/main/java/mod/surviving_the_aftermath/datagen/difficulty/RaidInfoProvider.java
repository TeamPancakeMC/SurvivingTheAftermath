package mod.surviving_the_aftermath.datagen.difficulty;

import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;

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
