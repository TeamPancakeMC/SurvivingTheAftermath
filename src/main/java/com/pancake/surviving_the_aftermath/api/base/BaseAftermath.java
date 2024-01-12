package com.pancake.surviving_the_aftermath.api.base;

import com.google.common.collect.Sets;
import com.pancake.surviving_the_aftermath.api.AftermathManager;
import com.pancake.surviving_the_aftermath.api.AftermathState;
import com.pancake.surviving_the_aftermath.api.IAftermath;
import com.pancake.surviving_the_aftermath.api.module.IAftermathModule;
import com.pancake.surviving_the_aftermath.api.module.IConditionModule;
import com.pancake.surviving_the_aftermath.common.data.pack.AftermathModuleLoader;
import com.pancake.surviving_the_aftermath.common.module.condition.StructureConditionModule;
import com.pancake.surviving_the_aftermath.util.RegistryUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.properties.StructureMode;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.StructureStart;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;

import java.util.*;
import java.util.function.Predicate;

public abstract class BaseAftermath<T extends BaseAftermathModule> implements IAftermath<BaseAftermathModule> {
    protected final AftermathManager MANAGER = AftermathManager.getInstance();
    protected AftermathState state;
    protected Level level;
    protected Set<UUID> players = Sets.newHashSet();
    protected Set<UUID> enemies = Sets.newHashSet();
    protected T module;
    protected final ServerBossEvent progress = new ServerBossEvent(Component.empty(), BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.PROGRESS);
    protected final UUID uuid = progress.getId();
    protected float progressPercent = progress.getProgress();


    public BaseAftermath(AftermathState state, Set<UUID> players, Set<UUID> enemies, T module, float progressPercent) {
        this.state = state;
        this.players = players;
        this.enemies = enemies;
        this.module = module;
        this.progressPercent = progressPercent;
    }
    public BaseAftermath(Level level, BlockPos pos, Player player){
        this.level = level;
        this.module = (T) getRandomAftermathModule();
        getSpawnPos(level,pos);
    }


    public BaseAftermath() {
    }

    public IAftermathModule getRandomAftermathModule() {
        Collection<IAftermathModule> modules = AftermathModuleLoader.AFTERMATH_MODULE_MAP.get(getRegistryName());
        return modules.stream().findAny().get();
    }

    public void getSpawnPos(Level level, BlockPos pos) {
        if (level instanceof ServerLevel serverLevel){
            Optional<IConditionModule> module = getModule().getConditions().stream()
                    .filter(condition -> condition instanceof StructureConditionModule structureModule)
                    .findFirst();
            module.ifPresent(structureModule ->{
                if (structureModule instanceof StructureConditionModule structureConditionModule){
                    if (structureConditionModule.checkCondition(level,pos)) {
                        String moduleStructure = structureConditionModule.getStructure();
                        ResourceKey<Structure> key = RegistryUtil.keyStructure(moduleStructure);
                        Structure structure = level.registryAccess().registryOrThrow(Registries.STRUCTURE).get(key);

                        StructureManager structureManager1 = serverLevel.structureManager();
                        if (structure != null) {
                            StructureStart structureAt = structureManager1.getStructureAt(pos, structure);
                            ChunkPos chunkPos = structureAt.getChunkPos();
                            List<StructurePiece> pieces = structureAt.getPieces();
                            for (StructurePiece piece : pieces) {
                                BlockPos realPos = piece.getLocatorPosition().offset(chunkPos.x * 16, 0, chunkPos.z * 16); // 计算真实的世界坐标
                                System.out.println("realPos : " + realPos + "BlockState : " + serverLevel.getBlockState(realPos).getBlock());
                            }
                        }
                    }
                }
            });
        }
    }

    @Override
    public boolean isCreate(Level level, BlockPos pos, Player player) {
        return getModule().isCreate(level, pos,player);
    }

    @Override
    public void tick() {
        if (isEnd()) return;

        updateProgress();
        if (state == AftermathState.VICTORY){
            this.progressPercent = 0;
            spawnRewards();
        }
    }

    @Override
    public void updateProgress() {
        progress.setProgress(progressPercent);
        updatePlayers();
    }

    @Override
    public void updatePlayers() {
        if (level instanceof ServerLevel serverLevel){
            final Set<ServerPlayer> oldPlayers = Sets.newHashSet(progress.getPlayers());
            final Set<ServerPlayer> newPlayers = Sets.newHashSet(serverLevel.getPlayers(this.validPlayer()));
            players.clear();
            newPlayers.stream()
                    .filter(player -> !oldPlayers.contains(player))
                    .forEach(progress::addPlayer);
            oldPlayers.stream()
                    .filter(player -> !newPlayers.contains(player))
                    .forEach(progress::removePlayer);
            progress.getPlayers().forEach(player -> players.add(player.getUUID()));
        }
    }

    @Override
    public Predicate<? super ServerPlayer> validPlayer() {
        return (Predicate<ServerPlayer>) player -> !player.isSpectator();
    }
    @Override
    public void spawnRewards() {

    }





    @Override
    public boolean isEnd() {
        return this.state == AftermathState.END;
    }
    @Override
    public boolean isLose() {
        return this.state == AftermathState.LOSE;
    }
    @Override
    public AftermathState getState() {
        return state;
    }
    @Override
    public Level getLevel() {
        return level;
    }
    @Override
    public Set<UUID> getPlayers() {
        return players;
    }
    @Override
    public Set<UUID> getEnemies() {
        return enemies;
    }
    @Override
    public T getModule() {
        return module;
    }
    @Override
    public UUID getUUID() {
        return uuid;
    }
    @Override
    public float getProgressPercent() {
        return progressPercent;
    }
}