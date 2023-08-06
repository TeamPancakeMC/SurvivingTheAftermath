package mod.surviving_the_aftermath.data;

import java.util.List;
import java.util.Map;

public class ModDifficultyLoader {
    public static class Hard extends DifficultyLoader{
        public Hard() {
            super("raid/hard");
        }
    }

    public static class Normal extends DifficultyLoader{
        public Normal() {
            super("raid/normal");
        }
    }

    public static class Easy extends DifficultyLoader{
        public Easy() {
            super("raid/easy");
        }
    }

    public static class Peaceful extends DifficultyLoader{
        public Peaceful() {
            super("raid/peaceful");
        }
    }


    public static Map<String, List<RaidInfo>> getRaidInfoMap(int difficulty){
        return switch (difficulty) {
            case 0 -> new Peaceful().getRaidMap();
            case 1 -> new Easy().getRaidMap();
            case 2 -> new Normal().getRaidMap();
            case 3 -> new Hard().getRaidMap();
            default -> throw new IllegalStateException("Unexpected value: " + difficulty);
        };
    }
    public static List<RaidInfo> getRaidInfoMap(String id,int difficulty){
        return switch (difficulty) {
            case 0 -> new Peaceful().getRaidMap().get(id);
            case 1 -> new Easy().getRaidMap().get(id);
            case 2 -> new Normal().getRaidMap().get(id);
            case 3 -> new Hard().getRaidMap().get(id);
            default -> throw new IllegalStateException("Unexpected value: " + difficulty + " " + id);
        };
    }


}
