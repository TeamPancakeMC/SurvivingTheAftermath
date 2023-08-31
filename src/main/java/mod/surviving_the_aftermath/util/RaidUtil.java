package mod.surviving_the_aftermath.util;

import mod.surviving_the_aftermath.data.RaidEnemyInfo;
import mod.surviving_the_aftermath.raid.RaidFactory;

import java.util.List;
import java.util.Random;

public class RaidUtil {
    public static RaidEnemyInfo getRaidEnemyInfo(int difficulty, String identifier) {
        List<RaidEnemyInfo> raidInfo = RaidFactory.getDifficulty(difficulty).getRaidInfo(identifier);
        return raidInfo.get(new Random().nextInt(raidInfo.size()));
    }

    public static int getRaidEnemyInfoIndex(int difficulty, String identifier, RaidEnemyInfo info) {
        List<RaidEnemyInfo> raidInfo = RaidFactory.getDifficulty(difficulty).getRaidInfo(identifier);
        return raidInfo.indexOf(info);
    }

    public static RaidEnemyInfo getRaidEnemyInfo(int id, String identifier, int indexEnemyInfo) {
        List<RaidEnemyInfo> raidInfo = RaidFactory.getDifficulty(id).getRaidInfo(identifier);
        return raidInfo.get(indexEnemyInfo);
    }
}
