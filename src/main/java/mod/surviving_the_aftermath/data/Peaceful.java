package mod.surviving_the_aftermath.data;

public class Peaceful extends ModDifficultyLoader {
    private static final Peaceful INSTANCE = new Peaceful();

    private Peaceful() {
        super("raid/peaceful");
    }

    public static Peaceful getInstance() {
        return INSTANCE;
    }
}