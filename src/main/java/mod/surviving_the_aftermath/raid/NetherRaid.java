package mod.surviving_the_aftermath.raid;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.capability.RaidData;
import mod.surviving_the_aftermath.data.ModDifficultyLoader;
import mod.surviving_the_aftermath.data.RaidInfo;
import mod.surviving_the_aftermath.data.WaveEntry;
import mod.surviving_the_aftermath.event.MobBattleTrackerEventSubscriber;
import mod.surviving_the_aftermath.event.PlayerBattleTrackerEventSubscriber;
import mod.surviving_the_aftermath.event.RaidEvent;
import mod.surviving_the_aftermath.init.ModSoundEvents;
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
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Ghast;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.TemplateStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.*;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class NetherRaid {
//
//	private static final SimpleWeightedRandomList<Item> REWARDS = SimpleWeightedRandomList.<Item>builder()
//			.add(Items.GOLD_INGOT, 100).add(Items.DIAMOND, 10).add(Items.EMERALD, 20)
//			.add(Items.ENCHANTED_GOLDEN_APPLE, 2).add(Items.NETHERITE_SCRAP, 2)
//			.add(ModItems.NETHER_CORE.get(), 1).build();
//
//	private static final List<List<WaveEntry>> WAVES = List.of(
//			List.of(new WaveEntry(EntityType.PIGLIN, 4, 5, List.of())),
//
//			List.of(new WaveEntry(EntityType.PIGLIN, 4, 5, List.of()),
//					new WaveEntry(EntityType.HOGLIN, 1, 1, List.of())),
//
//			List.of(new WaveEntry(EntityType.PIGLIN, 4, 5, List.of()),
//					new WaveEntry(EntityType.HOGLIN, 1, 2, List.of()),
//					new WaveEntry(EntityType.MAGMA_CUBE, 1, 1, List.of())),
//
//			List.of(new WaveEntry(EntityType.PIGLIN, 4, 5, List.of()),
//					new WaveEntry(EntityType.HOGLIN, 1, 2, List.of()),
//					new WaveEntry(EntityType.MAGMA_CUBE, 1, 1, List.of()),
//					new WaveEntry(EntityType.BLAZE, 1, 1, List.of())),
//
//			List.of(new WaveEntry(EntityType.PIGLIN, 3, 4, List.of()),
//					new WaveEntry(EntityType.HOGLIN, 1, 2, List.of()),
//					new WaveEntry(EntityType.MAGMA_CUBE, 1, 2, List.of()),
//					new WaveEntry(EntityType.BLAZE, 1, 1, List.of()),
//					new WaveEntry(EntityType.PIGLIN_BRUTE, 1, 1,
//							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
//									Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD))),
//
//			List.of(new WaveEntry(EntityType.PIGLIN, 3, 4,
//					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
//							Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD)),
//					new WaveEntry(EntityType.HOGLIN, 2, 3, List.of()),
//					new WaveEntry(EntityType.MAGMA_CUBE, 1, 2, List.of()),
//					new WaveEntry(EntityType.GHAST, 1, 3, List.of()),
//					new WaveEntry(EntityType.BLAZE, 1, 1, List.of()),
//					new WaveEntry(EntityType.PIGLIN_BRUTE, 1, 1,
//							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
//									Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD))),
//
//
//			List.of(new WaveEntry(EntityType.PIGLIN, 3, 4,
//					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
//							Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD)),
//					new WaveEntry(EntityType.HOGLIN, 2, 3, List.of()),
//					new WaveEntry(EntityType.MAGMA_CUBE, 2, 3, List.of()),
//					new WaveEntry(EntityType.BLAZE, 2, 2, List.of()),
//					new WaveEntry(EntityType.GHAST, 1, 3, List.of()),
//					new WaveEntry(EntityType.PIGLIN_BRUTE, 2, 2,
//							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
//									Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD))),
//
//			List.of(new WaveEntry(EntityType.PIGLIN, 2, 3,
//					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
//							Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD)),
//					new WaveEntry(EntityType.HOGLIN, 2, 3, List.of()),
//					new WaveEntry(EntityType.MAGMA_CUBE, 2, 3, List.of()),
//					new WaveEntry(EntityType.GHAST, 1, 3, List.of()),
//					new WaveEntry(EntityType.BLAZE, 2, 3, List.of()),
//					new WaveEntry(EntityType.PIGLIN_BRUTE, 2, 3,
//							List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
//									Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD))),
//
//
//			List.of(new WaveEntry(EntityType.PIGLIN, 2, 3,
//					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
//							Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD)),
//					new WaveEntry(EntityType.HOGLIN, 3, 3, List.of()),
//					new WaveEntry(EntityType.MAGMA_CUBE, 3, 4, List.of()),
//					new WaveEntry(EntityType.GHAST, 1, 3, List.of()),
//					new WaveEntry(EntityType.BLAZE, 3, 4, List.of()),
//					new WaveEntry(EntityType.PIGLIN_BRUTE, 2, 3,
//							List.of(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS,
//									Items.NETHERITE_BOOTS, Items.GOLDEN_SWORD))),
//
//
//			List.of(new WaveEntry(EntityType.PIGLIN, 2, 3,
//					List.of(Items.GOLDEN_HELMET, Items.GOLDEN_CHESTPLATE, Items.GOLDEN_LEGGINGS,
//							Items.GOLDEN_BOOTS, Items.GOLDEN_SWORD)),
//					new WaveEntry(EntityType.HOGLIN, 3, 4, List.of()),
//					new WaveEntry(EntityType.GHAST, 1, 3, List.of()),
//					new WaveEntry(EntityType.MAGMA_CUBE, 3, 4, List.of()),
//					new WaveEntry(EntityType.BLAZE, 3, 4, List.of()),
//					new WaveEntry(EntityType.PIGLIN_BRUTE, 3, 4,
//							List.of(Items.NETHERITE_HELMET, Items.NETHERITE_CHESTPLATE, Items.NETHERITE_LEGGINGS,
//									Items.NETHERITE_BOOTS, Items.GOLDEN_SWORD))));

	public static final Codec<NetherRaid> CODEC = RecordCodecBuilder.create(instance -> instance
			.group(Codec.INT.fieldOf("wave").forGetter(NetherRaid::getWave),
					Codec.list(BlockPos.CODEC).fieldOf("spawn").forGetter(NetherRaid::getSpawn),
					Codec.list(UUIDUtil.CODEC).xmap(HashSet::new, ArrayList::new).fieldOf("enemies")
							.forGetter(NetherRaid::getEnemies),
					Codec.INT.fieldOf("totalEnemyCount").forGetter(NetherRaid::getTotalEnemyCount),
					Codec.STRING.fieldOf("keyFoRaidInfo").forGetter(NetherRaid::getKeyFoRaidInfo),
					Codec.INT.fieldOf("difficulty").forGetter(NetherRaid::getDifficulty)
			)
			.apply(instance, NetherRaid::new));

	public static final String NAME = Main.MODID + "." + "nether_raid";

	private final ServerBossEvent progress = new ServerBossEvent(Component.translatable(NAME),
			BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
	private int difficulty;
	private List<BlockPos> spawn = new ArrayList<>();
	private List<UUID> players = new ArrayList<>();
	private HashSet<UUID> enemies = new HashSet<>();
	private int wave;
	private int totalEnemyCount;
	private int delay; // Delay to make sure raid mobs have been re-added to the world after load
	private RaidState state;

	private static int REWARD_TIME = 20 * 10;
	private static final UUID RAID_ID = UUID.randomUUID();
	private final BlockPos centerPos;
	private final static int RANGE = 50;
	private static final PlayerBattleTrackerEventSubscriber playerBattleTrackerEventSubscriber = new PlayerBattleTrackerEventSubscriber(RAID_ID);
	private static final MobBattleTrackerEventSubscriber mobBattleTrackerEventSubscriber = new MobBattleTrackerEventSubscriber(RAID_ID);

	private RaidInfo raidInfo;
	private String keyFoRaidInfo;

	public NetherRaid(int wave, List<BlockPos> spawn, HashSet<UUID> enemies, int totalEnemyCount,String keyFoRaidInfo,int difficulty) {
		this.wave = wave;
		this.spawn = spawn;
		this.centerPos = spawn.get(0);
		this.enemies = enemies;
		this.totalEnemyCount = totalEnemyCount;
		this.keyFoRaidInfo = keyFoRaidInfo;
		this.difficulty = difficulty;
		this.raidInfo = ModDifficultyLoader.getRaidInfo(keyFoRaidInfo,difficulty);
		this.delay = 100;
		this.state = RaidState.START;
		registryTracker();
	}

	public NetherRaid(BlockPos pos, ServerLevel level) {
		this.centerPos = pos;
		this.difficulty = level.getDifficulty().getId();
		List<RaidInfo> netherRaid = ModDifficultyLoader.getRaidInfoList("nether_raid", difficulty);
        this.raidInfo = netherRaid.get(level.getRandom().nextInt(netherRaid.size()));
		this.keyFoRaidInfo = ModDifficultyLoader.getRaidInfoString(raidInfo,difficulty);
		setSpawn(pos, level);
		updatePlayers(level);
		this.state = RaidState.START;
		MinecraftForge.EVENT_BUS.post(new RaidEvent.Start(players, level));
		updateProgress(level);
		registryTracker();
	}


	public UUID getRaidId() {
		return RAID_ID;
	}

	private void registryTracker() {
		MinecraftForge.EVENT_BUS.register(playerBattleTrackerEventSubscriber);
		MinecraftForge.EVENT_BUS.register(mobBattleTrackerEventSubscriber);


		RaidData.PlayerRegistryTracker(RAID_ID,playerBattleTrackerEventSubscriber);
		RaidData.MobRegistryTracker(RAID_ID,mobBattleTrackerEventSubscriber);
	}

	private void setSpawn(BlockPos pos, ServerLevel level) {
		Set<BlockPos> found = new HashSet<>();
		LinkedList<BlockPos> list = new LinkedList<>();
		list.add(pos);
		while (!list.isEmpty()) {
			BlockPos p = list.pop();
			if (found.contains(p)) {
				continue;
			}
			found.add(p);
			for (Direction direction : Direction.values()) {
				BlockPos nearby = p.relative(direction);
				if (found.contains(nearby) || !level.getBlockState(nearby).is(Blocks.NETHER_PORTAL)) {
					continue;
				}
				list.add(nearby);
			}
		}

		spawn = new ArrayList<>(found);
	}

	public void tick(ServerLevel level) {
		if (loseOrEnd()) {
			progress.removeAllPlayers();
			return;
		}
		progress.setVisible(this.state == RaidState.ONGOING);
		if (delay > 0) {
			delay--;
		}
		if (delay == 0) {
			if (this.state != RaidState.VICTORY) {
				setState(RaidState.ONGOING);
				MinecraftForge.EVENT_BUS.post(new RaidEvent.Ongoing(players, level));
				updatePlayers(level);
				updateProgress(level);
			} else {
				if (REWARD_TIME > 0) {
					REWARD_TIME--;
					setState(RaidState.VICTORY);
					MinecraftForge.EVENT_BUS.post(new RaidEvent.Victory(players, level));
					spawnRewards(level);
				} else {
					setState(RaidState.END);
					MinecraftForge.EVENT_BUS.post(new RaidEvent.End(players, level));
				}
			}
		}
	}

	private void spawnRewards(ServerLevel level) {
		setState(RaidState.CELEBRATING);
		MinecraftForge.EVENT_BUS.post(new RaidEvent.Celebrating(players, level));
		BlockPos pos = spawn.get(level.random.nextInt(spawn.size()));
		Direction dir = freeDirection(level, pos);
		Vec3 vec = Vec3.atCenterOf(pos);
		this.raidInfo.getRewards().getRandomValue(level.random).ifPresent(reward ->
				level.addFreshEntity(new ItemEntity(level, vec.x, vec.y, vec.z, new ItemStack(reward),
						dir.getStepX() * 0.2, 0.2, dir.getStepZ() * 0.2f)));
		setState(RaidState.VICTORY);
	}

	private Direction freeDirection(ServerLevel level, BlockPos pos) {
		return Direction.Plane.HORIZONTAL.stream().filter(d -> level.isEmptyBlock(pos.relative(d))
				&& !spawn.contains(pos.relative(d))).findFirst().orElse(Direction.UP);
	}

	private void updateProgress(ServerLevel level) {
		if (players.isEmpty()) {
			return;
		}

		enemies.removeIf(id -> {
			Entity entity = level.getEntity(id);
			if (entity == null || entity.isRemoved()) {
				return true;
			} else {
				if (entity instanceof Mob mob && mob.getTarget() == null && !players.isEmpty()) {
					Player target = level.getPlayerByUUID(players.get(level.getRandom().nextInt(players.size())));
					mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, target.getUUID());
				}
				return false;
			}
		});

		if (enemies.isEmpty()) {
			updateStructure(level);
			wave++;
			level.playSeededSound(null, spawn.get(0).getX(), spawn.get(0).getY(), spawn.get(0).getZ(),
					SoundEvents.GOAT_HORN_SOUND_VARIANTS.get(2), SoundSource.NEUTRAL, 2.0F, 1.0F, level.random.nextLong());
			List<List<WaveEntry>> waves = this.raidInfo.getWaves();
			if (wave > waves.size()) {
				setState(RaidState.VICTORY);
				level.playSeededSound(null, this.spawn.get(0).getX(), this.spawn.get(0).getY(), this.spawn.get(0).getZ(),
						ModSoundEvents.ORCHELIAS_VOX.get(), SoundSource.NEUTRAL, 3.0F, 1.0F, level.random.nextLong());
				playerBattleTrackerEventSubscriber.restorePlayerGameMode(level);
				MinecraftForge.EVENT_BUS.post(new RaidEvent.Victory(players, level));
				return;
			}
			spawnEnemies(level, waves.get(Math.min(waves.size() - 1, wave - 1)));
		}
		progress.setProgress(enemies.size() / (float) totalEnemyCount);
	}

	private void updateStructure(ServerLevel level) {
		StructureStart start = level.structureManager().getStructureAt(spawn.get(0), Objects.requireNonNull(level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID)));
		if (start != StructureStart.INVALID_START) {
			Optional<StructureTemplate> template = level.getStructureManager().get(NetherRaidStructure.STRUCTURE_TRANSFORMED);
			template.ifPresent(t -> {
				if (start.getPieces().get(0) instanceof TemplateStructurePiece piece) {
					BlockPos pos = piece.templatePosition();
					StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(piece.getRotation()).setMirror(Mirror.NONE)
							.addProcessor(new BlockIgnoreProcessor(ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK, Blocks.NETHER_PORTAL,Blocks.OBSIDIAN)))
							.addProcessor(new StructureProcessor() {

								@Override
								protected StructureProcessorType<?> getType() {
									return null;
								}

								@Override
								public StructureTemplate.StructureBlockInfo process(LevelReader levelReader,
										BlockPos p_74141_, BlockPos p_74142_,
										StructureTemplate.StructureBlockInfo blockInfo,
										StructureTemplate.StructureBlockInfo relativeBlockInfo,
										StructurePlaceSettings p_74145_,
										@Nullable StructureTemplate template) {
									if (level.getRandom().nextFloat() < 0.9) {
										return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(),
												levelReader.getBlockState(relativeBlockInfo.pos()), relativeBlockInfo.nbt());
									} else {
										return relativeBlockInfo;
									}
								}
							});
					t.placeInWorld(level, pos, pos, settings, level.random, 2);
//					progress.setName(wave == WAVES.size() ? Component.translatable(NAME + ".victory")
//							: Component.translatable(NAME + ".wave", wave));
				}
			});
		}
	}

	public void join(Entity entity) {
		if (entity.getType() == EntityType.MAGMA_CUBE && this.state == RaidState.ONGOING
				&& Math.sqrt(entity.blockPosition().distSqr(centerPos)) < RANGE && enemies.add(entity.getUUID())) {
			totalEnemyCount++;
			progress.setProgress(enemies.size() / (float) totalEnemyCount);
		}
	}

	private void spawnEnemies(ServerLevel level, List<WaveEntry> entries) {
		totalEnemyCount = 0;
		for (var entry : entries) {
			for (int i = 0; i < level.random.nextInt(entry.getMin(), entry.getMax() + 1); i++) {
				Entity enemy = entry.getType().create(level);

				enemy.moveTo(Vec3.atCenterOf(spawn.get(level.getRandom().nextInt(spawn.size()))));
				if (enemy instanceof AbstractPiglin piglin) {
					piglin.setImmuneToZombification(true);
				}
				if (enemy instanceof Hoglin hoglin) {
					hoglin.setImmuneToZombification(true);
				}
				if (enemy instanceof Slime slime) {
					slime.finalizeSpawn(level, level.getCurrentDifficultyAt(enemy.blockPosition()), MobSpawnType.EVENT, null, null);
				}
				if (enemy instanceof Mob mob) {
					for (var item : entry.getGear()) {
						if (level.random.nextFloat() < 0.5f) {
							mob.equipItemIfPossible(new ItemStack(item));
						}
					}
					if (enemy instanceof Ghast){
						enemy.setPos(enemy.getX(), enemy.getY() + 20, enemy.getZ());
					}
					for (var slot : EquipmentSlot.values()) {
						mob.setDropChance(slot, 0);
					}

					if (!players.isEmpty()) {
						Player target = level.getPlayerByUUID(players.get(level.getRandom().nextInt(players.size())));
						mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, target.getUUID());
						mob.setTarget(target);
					}
				}

				moveToEmptyAdjacentBlock(level, enemy);

				enemies.add(enemy.getUUID());
				level.addFreshEntity(enemy);
				totalEnemyCount++;
			}
		}
	}

	private boolean isBlocked(ServerLevel level, Entity entity) {
		for (var shape : level.getBlockCollisions(entity, entity.getBoundingBox())) {
			return !shape.isEmpty();
		}
		return false;
	}
	public void moveToEmptyAdjacentBlock(ServerLevel level, Entity entity) {
		if (isBlocked(level, entity)) {
			Direction.Plane.HORIZONTAL.stream().forEach(direction -> {
				if (level.isEmptyBlock(entity.blockPosition().relative(direction, 4))) {

					entity.moveTo(Vec3.atCenterOf(entity.blockPosition().relative(direction)));
				}
			});
		}
	}

	private void updatePlayers(ServerLevel level) {
		Set<UUID> updated = new HashSet<>();
		for (var player : level.players()) {

			if (Math.sqrt(player.distanceToSqr(Vec3.atCenterOf(centerPos))) < RANGE) {
				updated.add(player.getUUID());
			}
		}

		for (var id : updated) {
			if (!players.contains(id)) {
				Optional.ofNullable(level.getPlayerByUUID(id)).ifPresent(p -> progress.addPlayer((ServerPlayer) p));
			}
		}

		for (var id : players) {
			if (!updated.contains(id)) {
				Optional.ofNullable(level.getPlayerByUUID(id)).ifPresent(p -> progress.removePlayer((ServerPlayer) p));
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

	public RaidState getState() {
		return state;
	}

	public boolean loseOrEnd() {
		return state == RaidState.LOSE || state == RaidState.END;
	}

	public void setState(RaidState state) {
		this.state = state;
	}

	public BlockPos getCenterPos() {
		return centerPos;
	}

	public String getKeyFoRaidInfo() {
		return keyFoRaidInfo;
	}

	public int getDifficulty() {
		return difficulty;
	}
}