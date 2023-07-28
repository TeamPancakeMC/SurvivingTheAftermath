package mod.surviving_the_aftermath.raid;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.event.PlayerBattleTrackerEventSubscriber;
import mod.surviving_the_aftermath.event.RaidEvent;
import mod.surviving_the_aftermath.init.ModStructures;
import mod.surviving_the_aftermath.structure.NetherRaidStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.UUIDUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.BlockIgnoreProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessorType;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

public class NetherRaid {

	private static record WaveEntry(EntityType<?> type, int min, int max, List<Item> gear) {

	}

	private static final SimpleWeightedRandomList<Item> REWARDS = SimpleWeightedRandomList.<Item>builder()
			.add(Items.GOLD_INGOT, 100).add(Items.DIAMOND, 10).add(Items.EMERALD, 20)
			.add(Items.ENCHANTED_GOLDEN_APPLE, 2).add(Items.NETHERITE_SCRAP, 2).add(Items.ELYTRA, 1).build();

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
									Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD))),
			List.of(new WaveEntry(EntityType.PIGLIN, 3, 4,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
							Items.GOLDEN_SWORD)),
					new WaveEntry(EntityType.HOGLIN, 2, 3, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 1, 2, List.of()),
					new WaveEntry(EntityType.BLAZE, 1, 1, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 1, 1,
							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
									Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD))),
			List.of(new WaveEntry(EntityType.PIGLIN, 3, 4,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
							Items.GOLDEN_SWORD)),
					new WaveEntry(EntityType.HOGLIN, 2, 3, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 2, 3, List.of()),
					new WaveEntry(EntityType.BLAZE, 2, 2, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 2, 2,
							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
									Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD))),
			List.of(new WaveEntry(EntityType.PIGLIN, 2, 3,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
							Items.GOLDEN_SWORD)),
					new WaveEntry(EntityType.HOGLIN, 2, 3, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 2, 3, List.of()),
					new WaveEntry(EntityType.BLAZE, 2, 3, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 2, 3,
							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
									Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD))),
			List.of(new WaveEntry(EntityType.PIGLIN, 2, 3,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
							Items.GOLDEN_SWORD)),
					new WaveEntry(EntityType.HOGLIN, 3, 3, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 3, 4, List.of()),
					new WaveEntry(EntityType.BLAZE, 3, 4, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 2, 3,
							List.of(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS,
									Items.NETHERITE_BOOTS, Items.GOLDEN_SWORD))),
			List.of(new WaveEntry(EntityType.PIGLIN, 2, 3,
					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS, Items.GOLDEN_BOOTS,
							Items.GOLDEN_SWORD)),
					new WaveEntry(EntityType.HOGLIN, 3, 4, List.of()),
					new WaveEntry(EntityType.MAGMA_CUBE, 3, 4, List.of()),
					new WaveEntry(EntityType.BLAZE, 3, 4, List.of()),
					new WaveEntry(EntityType.PIGLIN_BRUTE, 3, 4,
							List.of(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS,
									Items.NETHERITE_BOOTS, Items.GOLDEN_SWORD))));

	public static final Codec<NetherRaid> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Codec.INT.fieldOf("wave").forGetter(NetherRaid::getWave),
					Codec.list(BlockPos.CODEC).fieldOf("spawn").forGetter(NetherRaid::getSpawn),
					Codec.list(UUIDUtil.CODEC).xmap(l -> new HashSet<>(l), s -> new ArrayList<>(s)).fieldOf("enemies")
							.forGetter(NetherRaid::getEnemies),
					Codec.INT.fieldOf("totalEnemyCount").forGetter(NetherRaid::getTotalEnemyCount),
					Codec.INT.fieldOf("victoryTimer").forGetter(NetherRaid::getVictoryTimer))
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
	private int victoryTimer;
	private int delay; // Delay to make sure raid mobs have been re-added to the world after load
	private final int RADIUS = 50;

	public NetherRaid(int wave, List<BlockPos> spawn, HashSet<UUID> enemies, int totalEnemyCount, int victoryTimer) {
		this.wave = wave;
		this.spawn = spawn;
		this.enemies = enemies;
		this.totalEnemyCount = totalEnemyCount;
		this.victoryTimer = victoryTimer;
		this.delay = 100;
		MinecraftForge.EVENT_BUS.register(new PlayerBattleTrackerEventSubscriber());

	}

	public NetherRaid(BlockPos pos, ServerLevel level) {
		setSpawn(pos, level);
		updatePlayers(level);
		MinecraftForge.EVENT_BUS.post(new RaidEvent.Start(players, level));
		updateProgress(level);
		MinecraftForge.EVENT_BUS.register(new PlayerBattleTrackerEventSubscriber());

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
		progress.setVisible(!isVictory());

		if (delay > 0)
			delay--;
		timer++;
		if (isVictory())
			victoryTimer--;

		if (timer % 1 == 0 && delay == 0) {
			if (!isVictory()) {
				MinecraftForge.EVENT_BUS.post(new RaidEvent.Ongoing(players, level));
				updatePlayers(level);
				updateProgress(level);
			} else {
				spawnRewards(level);
			}
		}
	}

	private void spawnRewards(ServerLevel level) {
		MinecraftForge.EVENT_BUS.post(new RaidEvent.Victory(players, level));
		for (int i = 0; i < 5; i++) {
			var pos = spawn.get(level.random.nextInt(spawn.size()));
			var dir = freeDirection(level, pos);
			var vec = Vec3.atCenterOf(pos);
			REWARDS.getRandomValue(level.random).ifPresent(reward -> {
				level.addFreshEntity(new ItemEntity(level, vec.x, vec.y, vec.z, new ItemStack(reward),
						dir.getStepX() * 0.2, 0.2, dir.getStepZ() * 0.2f));
			});
		}
	}

	private Direction freeDirection(ServerLevel level, BlockPos pos) {
		return Direction.Plane.HORIZONTAL.stream()
				.filter(d -> level.isEmptyBlock(pos.relative(d)) && !spawn.contains(pos.relative(d))).findFirst()
				.orElse(Direction.UP);

	}

	private void updateProgress(ServerLevel level) {
		if (players.isEmpty())
			return;

		enemies.removeIf(id -> {
			var entity = level.getEntity(id);
			if (entity == null || entity.isRemoved()) {
				return true;
			} else {
				if (entity instanceof Mob mob && mob.getTarget() == null && !players.isEmpty()) {
					var target = level.getPlayerByUUID(players.get(level.getRandom().nextInt(players.size())));
					mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, target.getUUID());
				}
				return false;
			}
		});

		if (enemies.isEmpty()) {
			updateStructure(level);

			wave++;
			level.playSeededSound(null, spawn.get(0).getX(), spawn.get(0).getY(), spawn.get(0).getZ(),
					SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2), SoundSource.NEUTRAL, 2, 1, level.random.nextLong());
			if (wave > WAVES.size()) {
				victoryTimer = 20 * 20;
				return;
			}
			spawnEnemies(level, WAVES.get(Math.min(WAVES.size() - 1, wave - 1)));
		}
		progress.setProgress(enemies.size() / (float) totalEnemyCount);
	}

	private void updateStructure(ServerLevel level) {
		var start = level.structureManager().getStructureAt(spawn.get(0),
				level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID));
		if (start != StructureStart.INVALID_START) {
			var template = level.getStructureManager().get(NetherRaidStructure.STRUCTURE_TRANSFORMED);
			template.ifPresent(t -> {
				if (start.getPieces().get(0) instanceof TemplateStructurePiece piece) {
					var pos = piece.templatePosition();
					var settings = new StructurePlaceSettings().setRotation(piece.getRotation()).setMirror(Mirror.NONE)
							.addProcessor(new BlockIgnoreProcessor(
									ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK, Blocks.NETHER_PORTAL)))
							.addProcessor(new StructureProcessor() {

								@Override
								protected StructureProcessorType<?> getType() {
									return null;
								}

								@Override
								public StructureTemplate.StructureBlockInfo processBlock(LevelReader pLevel,
										BlockPos p_74300_, BlockPos pPos,
										StructureTemplate.StructureBlockInfo pBlockInfo,
										StructureTemplate.StructureBlockInfo pRelativeBlockInfo,
										StructurePlaceSettings pSettings) {
									return level.random.nextFloat() < 0.9
											? new StructureTemplate.StructureBlockInfo(pRelativeBlockInfo.pos(),
													pLevel.getBlockState(pRelativeBlockInfo.pos()), null)
											: pRelativeBlockInfo;
								}
							});
					t.placeInWorld(level, pos, pos, settings, level.random, 2);
					progress.setName(wave == WAVES.size() ? Component.translatable(NAME + ".victory")
							: Component.translatable(NAME + ".wave", wave));
				}
			});
		}
	}

	public void join(Entity entity) {
		if (entity.getType() == EntityType.MAGMA_CUBE && !isVictory()
				&& entity.blockPosition().distSqr(spawn.get(0)) < 25 * 25 && enemies.add(entity.getUUID())) {
			totalEnemyCount++;
			progress.setProgress(enemies.size() / (float) totalEnemyCount);
		}
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
						if (level.random.nextFloat() < 0.5f) {
							mob.equipItemIfPossible(new ItemStack(item));
						}
					}

					for (var slot : EquipmentSlot.values()) {
						mob.setDropChance(slot, 0);
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
				enemies.add(enemy.getUUID());
				level.addFreshEntity(enemy);
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
			if (player.distanceToSqr(Vec3.atCenterOf(spawn.get(0))) < RADIUS * RADIUS) {
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

	public int getVictoryTimer() {
		return victoryTimer;
	}

	public boolean isVictory() {
		return victoryTimer > 0;
	}

	public boolean isDone() {
		return victoryTimer == 1;
	}
}
