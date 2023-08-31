package mod.surviving_the_aftermath.data;

public class Normal extends ModDifficultyLoader {
    private static final Normal INSTANCE = new Normal();

    private Normal() {
        super("raid/normal");
    }

    public static Normal getInstance() {
        return INSTANCE;
    }
}