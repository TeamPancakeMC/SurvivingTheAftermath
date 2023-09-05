package mod.surviving_the_aftermath.raid;

import com.google.common.collect.ImmutableList;
import mod.surviving_the_aftermath.data.RaidEnemyInfo;
import mod.surviving_the_aftermath.init.ModStructures;
import mod.surviving_the_aftermath.structure.NetherRaidStructure;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
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

import javax.annotation.Nullable;
import java.util.*;

public class NetherRaid extends BaseRaid {
    public final static String IDENTIFIER = "nether_raid";
    public NetherRaid(ServerLevel level, CompoundTag nbt) {
        super(level, nbt);
    }

    public NetherRaid(ServerLevel level, UUID uuid, BlockPos center) {
        super(level, uuid, center);
    }

    @Override
    public String getIdentifier() {
        return IDENTIFIER;
    }

    @Override
    public boolean isCreate(ServerLevel level) {
        return level.structureManager().getAllStructuresAt(this.centerPos)
                .containsKey(level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID));
    }

    @Override
    public void updateStructure() {
        StructureStart start = this.LEVEL.structureManager().getStructureAt(centerPos, Objects.requireNonNull(this.LEVEL.registryAccess().registryOrThrow(Registries.STRUCTURE).get(ModStructures.NETHER_RAID)));
        if (start != StructureStart.INVALID_START) {
            Optional<StructureTemplate> template = this.LEVEL.getStructureManager().get(NetherRaidStructure.STRUCTURE_TRANSFORMED);
            template.ifPresent(t -> {
                if (start.getPieces().get(0) instanceof TemplateStructurePiece piece) {
                    BlockPos pos = piece.templatePosition();
                    StructurePlaceSettings settings = new StructurePlaceSettings().setRotation(piece.getRotation()).setMirror(Mirror.NONE)
                            .addProcessor(new BlockIgnoreProcessor(ImmutableList.of(Blocks.AIR, Blocks.STRUCTURE_BLOCK, Blocks.NETHER_PORTAL, Blocks.OBSIDIAN)))
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
                                    if (NetherRaid.super.random.nextFloat() < 0.9) {
                                        return new StructureTemplate.StructureBlockInfo(relativeBlockInfo.pos(),
                                                levelReader.getBlockState(relativeBlockInfo.pos()), relativeBlockInfo.nbt());
                                    } else {
                                        return relativeBlockInfo;
                                    }
                                }
                            });
                    t.placeInWorld(this.LEVEL, pos, pos, settings, this.LEVEL.random, 2);
                }
            });
        }
    }

    @Override
    public void spawnEnemies(List<RaidEnemyInfo.WaveEntry> entries) {
        totalEnemyCount = 0;
        for (var entry : entries) {
            for (int i = 0; i < this.random.nextInt(entry.min(), entry.max() + 1); i++) {
                Entity enemy = entry.type().create(this.LEVEL);

                if (enemy != null) {
                    BlockPos pos;
                    if (spawnPos.isEmpty()){
                        pos = centerPos;
                    }else {
                        pos = spawnPos.get(this.random.nextInt(spawnPos.size()));
                    }
                    enemy.moveTo(Vec3.atCenterOf(pos));
                }
                if (enemy instanceof AbstractPiglin piglin) {
                    piglin.setImmuneToZombification(true);
                }
                if (enemy instanceof Hoglin hoglin) {
                    hoglin.setImmuneToZombification(true);
                }
                if (enemy instanceof Slime slime) {
                    slime.finalizeSpawn(this.LEVEL, this.LEVEL.getCurrentDifficultyAt(enemy.blockPosition()), MobSpawnType.EVENT, null, null);
                }
                if (enemy instanceof Mob mob) {
                    for (var item : entry.gear()) {
                        if (this.random.nextFloat() < 0.5f) {
                            mob.equipItemIfPossible(new ItemStack(item));
                        }
                    }
                    if (enemy instanceof Ghast) {
                        enemy.setPos(enemy.getX(), enemy.getY() + 20, enemy.getZ());
                    }
                    for (var slot : EquipmentSlot.values()) {
                        mob.setDropChance(slot, 0);
                    }

                    if (!players.isEmpty()) {
                        Player target = this.LEVEL.getPlayerByUUID(players.get(this.random.nextInt(players.size())));
                        mob.getBrain().setMemory(MemoryModuleType.ANGRY_AT, target.getUUID());
                        mob.setTarget(target);
                    }
                }

                if (isBlocked(this.LEVEL, enemy)) {
                    Direction.Plane.HORIZONTAL.stream().forEach(direction -> {
                        if (this.LEVEL.isEmptyBlock(enemy.blockPosition().relative(direction, 4))) {
                            enemy.moveTo(Vec3.atCenterOf(enemy.blockPosition().relative(direction)));
                        }
                    });
                }

                enemies.add(enemy.getUUID());
                this.LEVEL.addFreshEntity(enemy);
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

    @Override
    public boolean join(Entity entity) {
        return entity.getType() == EntityType.MAGMA_CUBE && super.join(entity);
    }

    @Override
    public void setSpawnPos(ServerLevel level) {
        Set<BlockPos> found = new HashSet<>();
        Queue<BlockPos> queue = new LinkedList<>();
        queue.add(centerPos);

        while (!queue.isEmpty()) {
            BlockPos currentPos = queue.poll();
            if (found.contains(currentPos)) {
                continue;
            }
            found.add(currentPos);

            for (Direction direction : Direction.values()) {
                BlockPos nearby = currentPos.relative(direction);
                if (found.contains(nearby) || !level.getBlockState(nearby).is(Blocks.NETHER_PORTAL)) {
                    continue;
                }
                queue.add(nearby);
            }
        }

        spawnPos = new ArrayList<>(found);
    }
}
