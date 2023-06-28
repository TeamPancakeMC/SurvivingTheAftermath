package mod.surviving_the_aftermath.event;

import net.minecraft.core.BlockPos;
import net.minecraftforge.eventbus.api.Event;

public class RaidEvent extends Event {
    //突袭中心坐标
    public BlockPos centerPos;

    //突袭开始事件
    public static class Start extends RaidEvent {
    }
    //突袭结束事件
    public static class End extends RaidEvent {
    }
    //突袭正在进行事件
    public static class Ongoing extends RaidEvent {
    }
    //突袭胜利事件
    public static class Victory extends RaidEvent {
    }
    //突袭失败事件
    public static class Lose extends RaidEvent {
    }
    //庆祝中事件
    public static class Celebrating extends RaidEvent {
    }
    //庆祝结束事件
    public static class CelebrateEnd extends RaidEvent {
    }
}
