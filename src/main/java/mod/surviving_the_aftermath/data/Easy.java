package mod.surviving_the_aftermath.data;

public class Easy extends ModDifficultyLoader {
    private static final Easy INSTANCE = new Easy();

    private Easy() {
        super("raid/easy");
    }

    public static Easy getInstance() {
        return INSTANCE;
    }
}