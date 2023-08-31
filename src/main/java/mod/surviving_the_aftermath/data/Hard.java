package mod.surviving_the_aftermath.data;

public class Hard extends ModDifficultyLoader {
    private static final Hard INSTANCE = new Hard();

    private Hard() {
        super("raid/hard");
    }

    public static Hard getInstance() {
        return INSTANCE;
    }
}