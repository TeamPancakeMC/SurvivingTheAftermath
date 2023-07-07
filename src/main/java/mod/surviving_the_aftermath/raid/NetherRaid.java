package mod.surviving_the_aftermath.raid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class NetherRaid {

	public static final Codec<NetherRaid> CODEC = RecordCodecBuilder
			.create(instance -> instance
					.group(Codec.INT.fieldOf("wave").forGetter(NetherRaid::getWave),
							Codec.list(BlockPos.CODEC).fieldOf("spawn").forGetter(NetherRaid::getSpawn),
							Codec.list(UUIDUtil.CODEC).xmap(l -> new HashSet<>(l), s -> new ArrayList<>(s))
									.fieldOf("enemies").forGetter(NetherRaid::getEnemies))
					.apply(instance, NetherRaid::new));

	private ServerBossEvent progress = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.RED,
			BossEvent.BossBarOverlay.PROGRESS);
	private List<BlockPos> spawn = new ArrayList<>();
	private List<UUID> players = new ArrayList<>();
	private HashSet<UUID> enemies = new HashSet<>();
	private int timer;
	private int wave;
	private int delay; // Delay to make sure raid mobs have been re-added to the world after load

	public NetherRaid(int wave, List<BlockPos> spawn, HashSet<UUID> enemies) {
		this.wave = wave;
		this.spawn = spawn;
		this.enemies = enemies;
		this.delay = 100;
	}

	public NetherRaid(BlockPos pos, ServerLevel level) {
		setSpawn(pos, level);
		updatePlayers(level);
		updateProgress(level);
	}

	private void setSpawn(BlockPos pos, ServerLevel level) {
		Set<BlockPos> found = new HashSet<>();
		LinkedList<BlockPos> list = new LinkedList<>();
		list.add(pos);
		while (!list.isEmpty()) {
			var p = list.pop();
			if (found.contains(p))
				continue;
			found.add(p);
			for (var direction : Direction.values()) {
				var nearby = p.relative(direction);
				if (found.contains(nearby) || !level.getBlockState(nearby).is(Blocks.NETHER_PORTAL))
					continue;
				list.add(nearby);
			}
		}

		spawn = new ArrayList<>(found);
	}

	public void tick(ServerLevel level) {
		if (delay > 0)
			delay--;
		timer++;

		if (timer % 20 == 0 && delay == 0) {
			updatePlayers(level);
			updateProgress(level);
		}
	}

	private int enemiesForWave() {
		return (wave + 1) * 2;
	}

	private void updateProgress(ServerLevel level) {
		if (players.isEmpty())
			return;

		enemies.removeIf(id -> {
			var entity = level.getEntity(id);
			return entity == null || !entity.isAlive();
		});

		if (enemies.isEmpty()) {
			wave++;
			spawnEnemies(level);
		}
		progress.setProgress(enemies.size() / (float) enemiesForWave());
	}

	private void spawnEnemies(ServerLevel level) {
		for (int i = 0; i < enemiesForWave(); i++) {
			var piglin = EntityType.PIGLIN.create(level);
			piglin.moveTo(Vec3.atCenterOf(spawn.get(level.getRandom().nextInt(spawn.size()))));
			piglin.setImmuneToZombification(true);
			level.addFreshEntity(piglin);
			if (!players.isEmpty())
				piglin.setTarget(level.getPlayerByUUID(players.get(level.getRandom().nextInt(players.size()))));
			enemies.add(piglin.getUUID());
		}
	}

	private void updatePlayers(ServerLevel level) {
		Set<UUID> updated = new HashSet<>();
		for (var player : level.players()) {
			if (player.distanceToSqr(Vec3.atCenterOf(spawn.get(0))) < 30 * 30) {
				updated.add(player.getUUID());
			}
		}

		for (var id : updated) {
			if (!players.contains(id)) {
				Optional.ofNullable(level.getPlayerByUUID(id)).ifPresent(p -> {
					progress.addPlayer((ServerPlayer) p);
				});
			}
		}

		for (var id : players) {
			if (!updated.contains(id)) {
				Optional.ofNullable(level.getPlayerByUUID(id)).ifPresent(p -> {
					progress.removePlayer((ServerPlayer) p);
				});
			}
		}
		players = new ArrayList<>(updated);
	}

	public int getWave() {
		return wave;
	}

	public List<BlockPos> getSpawn() {
		return spawn;
	}

	public HashSet<UUID> getEnemies() {
		return enemies;
	}
}
