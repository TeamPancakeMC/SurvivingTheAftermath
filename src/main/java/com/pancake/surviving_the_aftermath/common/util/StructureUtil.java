package com.pancake.surviving_the_aftermath.common.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.*;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


public class StructureUtil {
    //StructureTemplate
    //获取模板尺寸
    public static int[] getSize(StructureTemplate structureTemplate) {
        return new int[]{structureTemplate.getSize().getX(), structureTemplate.getSize().getY(), structureTemplate.getSize().getZ()};
    }
    //获取模板方块信息
    public static StructureModule getStructureModule(StructureTemplate structureTemplate) {
        CompoundTag template = structureTemplate.save(new CompoundTag());
        ListTag blocks = template.getList("blocks", 10);
        List<StructureTemplate.StructureBlockInfo> structureBlockInfos = Lists.newArrayList();
        Map<Integer, BlockState> structurePalette = getStructurePalette(structureTemplate);
        blocks.stream()
                .map(tag -> (CompoundTag)tag)
                .forEach(block -> {
                    ListTag listtag = block.getList("pos", 3);
                    int x = listtag.getInt(0);
                    int y = listtag.getInt(1);
                    int z = listtag.getInt(2);

                    int state = block.getInt("state");

                    CompoundTag nbt = null;
                    if (block.contains("nbt")) {
                        nbt = block.getCompound("nbt");
                    }
                    structureBlockInfos.add(new StructureTemplate.StructureBlockInfo(new BlockPos(x, y, z), structurePalette.get(state), nbt));
                });
        StructureModule structureModule = new StructureModule(structureBlockInfos, structurePalette, null);
        System.out.println(structureModule.blocks.size());
        return structureModule;
    }

    //获取模板调色板信息
    public static Map<Integer,BlockState> getStructurePalette(StructureTemplate structureTemplate) {
        CompoundTag template = structureTemplate.save(new CompoundTag());
        HashMap<Integer,BlockState> map = Maps.newHashMap();
        ListTag palettesTag = template.getList("palette", 10);
        for (int i = 0; i < palettesTag.size(); i++) {
            CompoundTag palette = palettesTag.getCompound(i);
            BlockState blockState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), palette);
            map.put(i, blockState);
        }
        return map;
    }
    public static boolean randomCoordinatesCompareStructure(Level level, BlockPos currentPos,StructureModule structureModule) {
        BlockState currentState = level.getBlockState(currentPos);
        if (currentState.getBlock() == Blocks.AIR) {
            return false;
        }
        if (structureModule.palette.values().stream().anyMatch(state -> state.equals(currentState))) {
            for (StructureTemplate.StructureBlockInfo info : structureModule.blocks) {
                if (info.state().equals(currentState)) {
                    //获取可能的原点信息
                    BlockPos originPos = currentPos.offset(-info.pos().getX(), -info.pos().getY(), -info.pos().getZ());
                    //对比结构
                    if (compareStructure(level, originPos, structureModule)) {
                        System.out.println("对比成功");
                        return true;
                    }else {
                        System.out.println("对比失败");
                    }

                }
            }
        }
        return false;
    }
    public static boolean compareStructure(Level level, BlockPos originPos, StructureModule structureModule) {
        for (StructureTemplate.StructureBlockInfo info : structureModule.blocks) {
            BlockPos currentPos = originPos.offset(info.pos().getX(), info.pos().getY(), info.pos().getZ());
            BlockState currentState = level.getBlockState(currentPos);

//            if (info.state().isAir()) continue;
            if (!info.state().equals(currentState)) {
                System.out.println("currentState:" + currentState);
                System.out.println("info.state():" + info.state());
                return false;
            }
        }
        return true;
    }
    //记录类 StructureModule
    public record StructureModule(List<StructureTemplate.StructureBlockInfo> blocks,Map<Integer, BlockState> palette,List<StructureTemplate.StructureEntityInfo> entities) {
        public StructureModule clearAir() {
            List<StructureTemplate.StructureBlockInfo> blocks = Lists.newArrayList();
            this.blocks.forEach(block -> {
                if (!block.state().isAir()) {
                    blocks.add(block);
                }
            });
            Map<Integer, BlockState> palette = Maps.newHashMap();
            this.palette.forEach((key, value) -> {
                if (!value.isAir()) {
                    palette.put(key, value);
                }
            });
            return new StructureModule(blocks, palette, this.entities);
        }
    }
    //清除StructureModule里面的AIR

//    public static StructureModule createStructureModule(StructureTemplate structureTemplate) {
//        CompoundTag compoundTag = structureTemplate.save(new CompoundTag());
//        int[] moduleSize = new int[3];
//        ListTag size = compoundTag.getList("size", 3);
//        for (int i = 0; i < size.size(); i++) {
//            moduleSize[i] = size.getInt(i);
//        }
//
//        Map<Integer, BlockState> palettes = Maps.newHashMap();
//        compoundTag.getList("palette", 10).forEach(palette -> {
//            BlockState blockState = NbtUtils.readBlockState(BuiltInRegistries.BLOCK.asLookup(), (CompoundTag) palette);
//            palettes.put(compoundTag.getList("palette", 10).indexOf(palette), blockState);
//        });
//
//
//        List<StructureTemplate.StructureBlockInfo> blockInfos = Lists.newArrayList();
//        ListTag blocks = compoundTag.getList("blocks", 10);
//        for (int i = 0; i < blocks.size(); i++) {
//            CompoundTag block = blocks.getCompound(i);
//            int[] pos = new int[3];
//            ListTag posTag = block.getList("pos", 3);
//            int x = posTag.getInt(0);
//            int y = posTag.getInt(1);
//            int z = posTag.getInt(2);
//            BlockPos blockPos = new BlockPos(x, y, z);
//
//
//            Tag state = block.get("state");
//
//            BlockState blockState = palettes.get(((IntTag) state).getAsInt());
//
//            CompoundTag nbtTag = null;
//            if (block.contains("nbt")) {
//                nbtTag = block.getCompound("nbt");
//            }
//
//            blockInfos.add(new StructureTemplate.StructureBlockInfo(blockPos, blockState, nbtTag));
//        }

//        List<StructureTemplate.StructureEntityInfo> entityInfos = Lists.newArrayList();
//        ListTag entities = compoundTag.getList("entities", 10);
//        for (int i = 0; i < entities.size(); i++) {
//            CompoundTag entity = entities.getCompound(i);
//            CompoundTag nbt = (CompoundTag)entity.get("nbt");
//            double[] poseArray = new double[3];
//            int[] blockPosArray = new int[3];
//
//
//            ListTag pos = (ListTag)entity.get("pos");
//            assert pos != null;
//            pos.forEach(pose -> {
//                poseArray[pos.indexOf(pose)] = ((DoubleTag)pose).getAsDouble();
//            });
//
//
//            ListTag blockPos = (ListTag) entity.get("blockPos");
//            assert blockPos != null;
//            blockPos.forEach(blockPose -> {
//                blockPosArray[blockPos.indexOf(blockPose)] = ((IntTag)blockPose).getAsInt();
//            });
//
//            assert nbt != null;
//            EntityType<?> entityType = EntityType.byString(nbt.getString("id")).isPresent() ? EntityType.byString(nbt.getString("id")).get() : null;
//            entityInfos.add(new StructureTemplate.StructureEntityInfo(entityType, nbt, poseArray, blockPosArray));
//        }
//        return new StructureModule(blockInfos,palettes, null);
//    }
}
