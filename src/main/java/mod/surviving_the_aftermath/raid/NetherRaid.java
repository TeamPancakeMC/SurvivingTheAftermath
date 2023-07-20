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

import mod.surviving_the_aftermath.Main;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

public class NetherRaid {

	private static record WaveEntry(EntityType<?> type, int min, int max, List<Item> gear) {

	}

	private static final List<List<WaveEntry>> WAVES = List.of(
			List.of(new WaveEntry(EntityType.PIGLIN, 4, 5, List.of())),
			List.of(new WaveEntry(EntityType.PIGLIN, 4, 5, List.of()),
					new WaveEntry(EntityType.HOGLIN, 1, 1, List.of())),
			List.of(new WaveEntry(EntityType.PIGLIN, 4, 5, List.of()),
					new WaveEntry(EntityType.HOGLIN, 1, 2, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 1, 1, List.of())),
			List.of(new WaveEntry(EntityType.PIGLIN, 4, 5, List.of()),
					new WaveEntry(EntityType.HOGLIN, 1, 2, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 1, 1, List.of()),
					new WaveEntry(EntityType.BLAZE, 1, 1, List.of())),
			List.of(new WaveEntry(EntityType.PIGLIN, 3, 4, List.of()),
					new WaveEntry(EntityType.HOGLIN, 1, 2, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 1, 2, List.of()),
					new WaveEntry(EntityType.BLAZE, 1, 1, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 1, 1,
							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
									Items.GOLDEN_BOOTS))),
			List.of(new WaveEntry(EntityType.PIGLIN, 3, 4,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS)),
					new WaveEntry(EntityType.HOGLIN, 2, 3, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 1, 2, List.of()),
					new WaveEntry(EntityType.BLAZE, 1, 1, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 1, 1,
							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
									Items.GOLDEN_BOOTS))),
			List.of(new WaveEntry(EntityType.PIGLIN, 3, 4,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS)),
					new WaveEntry(EntityType.HOGLIN, 2, 3, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 2, 3, List.of()),
					new WaveEntry(EntityType.BLAZE, 2, 2, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 2, 2,
							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
									Items.GOLDEN_BOOTS))),
			List.of(new WaveEntry(EntityType.PIGLIN, 2, 3,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS)),
					new WaveEntry(EntityType.HOGLIN, 2, 3, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 2, 3, List.of()),
					new WaveEntry(EntityType.BLAZE, 2, 3, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 2, 3,
							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
									Items.GOLDEN_BOOTS))),
			List.of(new WaveEntry(EntityType.PIGLIN, 2, 3,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS)),
					new WaveEntry(EntityType.HOGLIN, 3, 3, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 3, 4, List.of()),
					new WaveEntry(EntityType.BLAZE, 3, 4, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 2, 3,
							List.of(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS,
									Items.NETHERITE_BOOTS))),
			List.of(new WaveEntry(EntityType.PIGLIN, 2, 3,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS)),
					new WaveEntry(EntityType.HOGLIN, 3, 4, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 3, 4, List.of()),
					new WaveEntry(EntityType.BLAZE, 3, 4, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 3, 4, List.of(Items.NETHERITE_HELMET,
							Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS, Items.NETHERITE_BOOTS))));

	public static final Codec<NetherRaid> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Codec.INT.fieldOf("wave").forGetter(NetherRaid::getWave),
					Codec.list(BlockPos.CODEC).fieldOf("spawn").forGetter(NetherRaid::getSpawn),
					Codec.list(UUIDUtil.CODEC).xmap(l -> new HashSet<>(l), s -> new ArrayList<>(s)).fieldOf("enemies")
							.forGetter(NetherRaid::getEnemies),
					Codec.INT.fieldOf("totalEnemyCount").forGetter(NetherRaid::getTotalEnemyCount))
			.apply(instance, NetherRaid::new));

	public static final String NAME = Main.MODID + "." + "nether_raid";

	private ServerBossEvent progress = new ServerBossEvent(Component.translatable(NAME), BossEvent.BossBarColor.RED,
			BossEvent.BossBarOverlay.PROGRESS);
	private List<BlockPos> spawn = new ArrayList<>();
	private List<UUID> players = new ArrayList<>();
	private HashSet<UUID> enemies = new HashSet<>();
	private int timer;
	private int wave;
	private int totalEnemyCount;
	private int delay; // Delay to make sure raid mobs have been re-added to the world after load

	public NetherRaid(int wave, List<BlockPos> spawn, HashSet<UUID> enemies, int totalEnemyCount) {
		this.wave = wave;
		this.spawn = spawn;
		this.enemies = enemies;
		this.totalEnemyCount = totalEnemyCount;
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

	private void updateProgress(ServerLevel level) {
		if (players.isEmpty())
			return;

		enemies.removeIf(id -> {
			var entity = level.getEntity(id);
			if (entity == null || !entity.isAlive()) {
				return true;
			} else {
				if (entity instanceof Mob mob && mob.getTarget() == null && !players.isEmpty()) {
					mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT,
							players.get(level.getRandom().nextInt(players.size())));
				}
				return false;
			}
		});

		if (enemies.isEmpty()) {
			wave++;
			spawnEnemies(level, WAVES.get(Math.min(WAVES.size() - 1, wave - 1)));
		}
		progress.setProgress(enemies.size() / (float) totalEnemyCount);
	}

	private void spawnEnemies(ServerLevel level, List<WaveEntry> entries) {
		totalEnemyCount = 0;
		for (var entry : entries) {
			for (int i = 0; i < level.random.nextInt(entry.min, entry.max + 1); i++) {
				var enemy = entry.type.create(level);
				enemy.moveTo(Vec3.atCenterOf(spawn.get(level.getRandom().nextInt(spawn.size()))));
				if (enemy instanceof AbstractPiglin piglin) {
					piglin.setImmuneToZombification(true);
				}
				if (enemy instanceof Hoglin hoglin) {
					hoglin.setImmuneToZombification(true);
				}
				if (enemy instanceof Slime slime) {
					slime.finalizeSpawn(level, level.getCurrentDifficultyAt(enemy.blockPosition()), MobSpawnType.EVENT,
							null, null);
				}
				if (enemy instanceof Mob mob) {
					for (var item : entry.gear) {
						if (level.random.nextFloat() < 0.5f)
							mob.equipItemIfPossible(new ItemStack(item));
					}
					if (!players.isEmpty()) {
						var target = level.getPlayerByUUID(players.get(level.getRandom().nextInt(players.size())));
						mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, target.getUUID());
						mob.setTarget(target);
					}
				}
				if (isBlocked(level, enemy)) {
					Direction.Plane.HORIZONTAL.stream().forEach(d -> {
						if (level.isEmptyBlock(enemy.blockPosition().relative(d, 2))) {
							enemy.moveTo(Vec3.atCenterOf(enemy.blockPosition().relative(d)));
						}
					});
				}
				level.addFreshEntity(enemy);
				enemies.add(enemy.getUUID());
				totalEnemyCount++;
			}
		}
	}

	private boolean isBlocked(ServerLevel level, Entity e) {
		for (var shape : level.getBlockCollisions(e, e.getBoundingBox())) {
			if (!shape.isEmpty()) {
				return true;
			}
		}
		return false;
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

	public int getTotalEnemyCount() {
		return totalEnemyCount;
	}
}
