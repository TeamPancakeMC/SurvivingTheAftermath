package mod.surviving_the_aftermath.raid;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.data.RaidEnemyInfo;
import mod.surviving_the_aftermath.event.RaidEvent;
import mod.surviving_the_aftermath.util.RaidUtil;
import mod.surviving_the_aftermath.event.battle_tracker.PlayerBattleTracker;
import mod.surviving_the_aftermath.init.ModSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.*;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import java.util.*;

public abstract class BaseRaid implements IRaid {
    protected final ServerLevel LEVEL;
    protected final HashSet<UUID> enemies = new HashSet<>();
    protected List<BlockPos> spawnPos = new ArrayList<>();
    protected String name = Main.MODID + "." + getIdentifier();
    protected final ServerBossEvent progress = new ServerBossEvent(Component.translatable(name), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    protected List<UUID> players = new ArrayList<>();
    public final Random random = new Random();
    protected UUID uuid;
    protected BlockPos centerPos;
    protected int totalEnemyCount;
    protected int indexEnemyInfo;
    protected int rewardTime;
    protected RaidEnemyInfo raidEnemyInfo;
    protected int wave;
    protected RaidState state;

    public BaseRaid(ServerLevel level, UUID uuid, BlockPos centerPos) {
        this.LEVEL = level;
        this.centerPos = centerPos;
        this.uuid = uuid;
        this.raidEnemyInfo = RaidUtil.getRaidEnemyInfo(level.getDifficulty().getId(), getIdentifier());
        this.indexEnemyInfo = RaidUtil.getRaidEnemyInfoIndex(level.getDifficulty().getId(), getIdentifier(), raidEnemyInfo);
        this.rewardTime = getRewardTime();
    }

    public BaseRaid(ServerLevel level, CompoundTag nbt) {
        this.LEVEL = level;
        this.deserializeNBT(nbt);
    }

    @Override
    public void create(ServerLevel level) {
        setSpawnPos(level);
        updatePlayers(level);
        updateProgress(level);
        this.state = RaidState.START;
        MinecraftForge.EVENT_BUS.post(new RaidEvent.Start(players, level));
    }

    @Override
    public void tick(ServerLevel level) {
        if (loseOrEnd()) {
            progress.removeAllPlayers();
            return;
        }
        progress.setVisible(this.state == RaidState.ONGOING);
        if (this.state != RaidState.VICTORY && this.state != RaidState.CELEBRATING) {
            setState(RaidState.ONGOING);
            MinecraftForge.EVENT_BUS.post(new RaidEvent.Ongoing(players, level));
            updatePlayers(level);
            updateProgress(level);
        } else {
            if (rewardTime > 0) {
                rewardTime--;
                spawnRewards(level);
            } else {
                setState(RaidState.END);
                MinecraftForge.EVENT_BUS.post(new RaidEvent.End(players, level));
            }
        }
    }

    @Override
    public UUID getUUID() {
        return uuid;
    }

    @Override
    public HashSet<UUID> getEnemies() {
        return enemies;
    }

    @Override
    public int getRadius() {
        return 50;
    }

    @Override
    public int getRewardTime() {
        return 20 * 10;
    }

    @Override
    public boolean join(Entity entity) {
        if (state == RaidState.ONGOING
                && Math.sqrt(entity.blockPosition().distSqr(centerPos)) < getRadius()
                && enemies.add(entity.getUUID())) {
            totalEnemyCount++;
            progress.setProgress(enemies.size() / (float) totalEnemyCount);
            return true;
        }
        return false;
    }

    @Override
    public void setSpawnPos(ServerLevel level) {
        spawnPos.add(centerPos);
    }

    @Override
    public void updatePlayers(ServerLevel level) {
        Set<UUID> updated = new HashSet<>();
        for (var player : level.players()) {

            if (Math.sqrt(player.distanceToSqr(Vec3.atCenterOf(centerPos))) < getRadius()) {
                updated.add(player.getUUID());
            }
        }

        for (var id : updated) {
            if (!players.contains(id)) {
                Optional.ofNullable(level.getPlayerByUUID(id)).ifPresent(player -> progress.addPlayer((ServerPlayer) player));
            }
        }

        for (var id : players) {
            if (!updated.contains(id)) {
                Optional.ofNullable(level.getPlayerByUUID(id)).ifPresent(player -> progress.removePlayer((ServerPlayer) player));
            }
        }
        players = new ArrayList<>(updated);
    }

    public void updateProgress(ServerLevel level) {
        if (players.isEmpty()) {
            return;
        }

        enemies.removeIf(id -> {
            Entity entity = level.getEntity(id);
            if (entity == null || entity.isRemoved()) {
                return true;
            }

            if (entity instanceof Mob mob && mob.getTarget() == null && !players.isEmpty()) {
                Player target = level.getPlayerByUUID(players.get(random.nextInt(players.size())));
                if (target != null) {
                    mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, target.getUUID());
                }
            }

            return false;
        });

        if (enemies.isEmpty()) {
            progress.setName(wave == raidEnemyInfo.waves().size() ? Component.translatable(name + ".victory") : Component.translatable(name + ".wave", wave));
            updateStructure();
            wave++;
            level.playSeededSound(null, centerPos.getX(), centerPos.getY(), centerPos.getZ(),
                    SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2), SoundSource.NEUTRAL, 2.0F, 1.0F, random.nextLong());
            List<List<RaidEnemyInfo.WaveEntry>> waves = this.raidEnemyInfo.waves();
            if (wave > waves.size()) {
                level.playSeededSound(null, centerPos.getX(), centerPos.getY(), centerPos.getZ(),
                        ModSoundEvents.ORCHELIAS_VOX.get(), SoundSource.NEUTRAL, 3.0F, 1.0F, random.nextLong());
                PlayerBattleTracker playerBattleTracker = RaidManager.getInstance().getPlayerBattleTracker(uuid);
                playerBattleTracker.restorePlayerGameMode(level);
                setState(RaidState.VICTORY);
                MinecraftForge.EVENT_BUS.post(new RaidEvent.Victory(players, level));
                return;
            }
            spawnEnemies(waves.get(Math.min(waves.size() - 1, wave - 1)));
        }
        progress.setProgress(enemies.size() / (float) totalEnemyCount);
    }

    @Override
    public void spawnRewards(ServerLevel level) {
        BlockPos pos;
        if (spawnPos.isEmpty()){
            pos = centerPos;
        }else {
            pos = spawnPos.get(random.nextInt(spawnPos.size()));
        }
        Direction dir = freeDirection(level, pos);
        Vec3 vec = Vec3.atCenterOf(pos);
        this.raidEnemyInfo.rewards().getRandomValue(level.random).ifPresent(reward ->
                level.addFreshEntity(new ItemEntity(level, vec.x, vec.y, vec.z, new ItemStack(reward),
                        dir.getStepX() * 0.2, 0.2, dir.getStepZ() * 0.2f)));
        setState(RaidState.CELEBRATING);
        MinecraftForge.EVENT_BUS.post(new RaidEvent.Celebrating(players, level));
    }


    private Direction freeDirection(ServerLevel level, BlockPos pos) {
        return Direction.Plane.HORIZONTAL.stream().filter(d -> level.isEmptyBlock(pos.relative(d))
                && !spawnPos.contains(pos.relative(d))).findFirst().orElse(Direction.UP);
    }

    @Override
    public RaidState getState() {
        return state;
    }

    @Override
    public void setState(RaidState state) {
        this.state = state;
    }

    @Override
    public boolean loseOrEnd() {
        return state == RaidState.LOSE || state == RaidState.END;
    }

    @Override
    public BlockPos getCenterPos() {
        return centerPos;
    }

    @Override
    public List<UUID> getPlayers() {
        return players;
    }

    @Override
    public UUID getUuid() {
        return uuid;
    }

    @Override
    public int getTotalEnemyCount() {
        return totalEnemyCount;
    }

    @Override
    public RaidEnemyInfo getRaidEnemyInfo() {
        return raidEnemyInfo;
    }

    @Override
    public int getWave() {
        return wave;
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag compoundTag = new CompoundTag();
        compoundTag.putString("identifier", getIdentifier());
        compoundTag.putUUID("uuid", uuid);
        compoundTag.put("centerPos", NbtUtils.writeBlockPos(centerPos));
        compoundTag.putInt("totalEnemyCount", totalEnemyCount);
        compoundTag.putInt("wave", wave);
        compoundTag.putInt("rewardTime", rewardTime);

        compoundTag.putInt("indexEnemyInfo", indexEnemyInfo);

        ListTag enemiesTags = new ListTag();
        enemies.forEach(uuid -> enemiesTags.add(NbtUtils.createUUID(uuid)));
        compoundTag.put("enemies", enemiesTags);

        ListTag spawnPosTags = new ListTag();
        spawnPos.forEach(pos -> spawnPosTags.add(NbtUtils.writeBlockPos(pos)));
        compoundTag.put("spawnPos", spawnPosTags);

        return compoundTag;
    }

    @Override
    public void deserializeNBT(CompoundTag nbt) {
        this.centerPos = NbtUtils.readBlockPos(nbt.getCompound("centerPos"));
        this.uuid = nbt.getUUID("uuid");
        this.totalEnemyCount = nbt.getInt("totalEnemyCount");
        this.wave = nbt.getInt("wave");
        this.rewardTime = nbt.getInt("rewardTime");

        this.indexEnemyInfo = nbt.getInt("indexEnemyInfo");
        this.raidEnemyInfo = RaidUtil.getRaidEnemyInfo(this.LEVEL.getDifficulty().getId(), getIdentifier(), indexEnemyInfo);

        ListTag enemiesTags = nbt.getList("enemies", 11);
        enemiesTags.forEach(tag -> {
            UUID uuid1 = NbtUtils.loadUUID(tag);
            enemies.add(uuid1);
        });

        ListTag spawnPosTags = nbt.getList("spawnPos", 11);
        spawnPosTags.forEach(tag -> spawnPos.add(NbtUtils.readBlockPos((CompoundTag) tag)));
    }
}
