package mod.surviving_the_aftermath.event;

import mod.surviving_the_aftermath.capability.RaidData;
import mod.surviving_the_aftermath.raid.NetherRaid;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashSet;
import java.util.UUID;

public class MobBattleTrackerEventSubscriber {
    private UUID currentRaidId;


    public MobBattleTrackerEventSubscriber(UUID currentRaidId) {
        this.currentRaidId = currentRaidId;
    }

    @SubscribeEvent
    public void onLivingRestrictedRange(LivingEvent.LivingTickEvent event) {
        NetherRaid netherRaid = RaidData.getNetherRaid(currentRaidId);
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide || netherRaid == null) return;
        HashSet<UUID> enemies = netherRaid.getEnemies();
        if (enemies.contains(entity.getUUID()) && entity instanceof Ghast) {
            BlockPos centerPos = netherRaid.getCenterPos();
            BlockPos pos = entity.blockPosition();
            double distance = Math.sqrt(centerPos.distSqr(pos));
            if (distance > 35) {
                double value = level.random.nextInt(20);
                entity.teleportTo(centerPos.getX() + value,centerPos.getY() + 20,centerPos.getZ() + value);
            }
        }
    }

    //添加高亮
    @SubscribeEvent
    public void onLivingAddHighlight(LivingEvent.LivingTickEvent event) {
        NetherRaid netherRaid = RaidData.getNetherRaid(currentRaidId);
        LivingEntity entity = event.getEntity();
        Level level = entity.level();
        if (level.isClientSide || netherRaid == null) return;
        HashSet<UUID> enemies = netherRaid.getEnemies();
        if (enemies.contains(entity.getUUID())) {
            entity.setGlowingTag(true);
        }
    }
}
