package com.pancake.surviving_the_aftermath.common.event;

import com.google.gson.JsonElement;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.JsonOps;
import com.pancake.surviving_the_aftermath.api.module.IAmountModule;
import com.pancake.surviving_the_aftermath.api.module.IEntityInfoModule;
import com.pancake.surviving_the_aftermath.common.init.ModAftermathModule;
import com.pancake.surviving_the_aftermath.common.init.ModuleRegistry;
import com.pancake.surviving_the_aftermath.common.module.amount.IntegerAmountModule;
import com.pancake.surviving_the_aftermath.common.module.amount.RandomAmountModule;
import com.pancake.surviving_the_aftermath.common.module.entity_info.EntityInfoModule;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;

@Mod.EventBusSubscriber
public class PlayerEvent {
    @SubscribeEvent
    public static void onPlayerInteractRightClickBlock(PlayerInteractEvent.RightClickBlock event) {
        Level level = event.getLevel();
        InteractionHand hand = event.getHand();
        if (level.isClientSide() || hand != InteractionHand.MAIN_HAND) {
            return;
        }

//        List<WeightedEntry.Wrapper<EntityType<?>>> entityList = Arrays.asList(
//                WeightedEntry.wrap(EntityType.CREEPER, 10),
//                WeightedEntry.wrap(EntityType.ZOMBIE, 20)
//        );
//

//
//        EntityTypeWeightedListModule module = new EntityTypeWeightedListModule();
//

//        module.Codec.encodeStart(opsNBT, entityList).result().ifPresent(nbt -> {
//            DataResult<List<WeightedEntry.Wrapper<EntityType<?>>>> parse = module.Codec.parse(opsNBT, nbt);
//            parse.result().ifPresent(list -> {
//                System.out.println("opsNBT List: " + list);
//            });
//            System.out.println("opsNBT nbt" + nbt);
//        });
//        Object o1 = module.transformedCodec.encodeStart(opsJSON, entityList).result().get();
//        System.out.println("opsNBT: " + o);
//        System.out.println("opsJSON: " + o1);
//
//        module.Codec.encodeStart(opsJSON, entityList).result().ifPresent(jsonElement -> {
//            DataResult<List<WeightedEntry.Wrapper<EntityType<?>>>> parse = module.Codec.parse(opsJSON, jsonElement);
//            parse.result().ifPresent(list -> {
//                System.out.println("opsJSON List: " + list);
//            });
//            System.out.println("opsJSON jsonElement: " + jsonElement);
//        });
//
//        module.transformedCodec.encodeStart(opsJSON, entityList).result().ifPresent(json -> {
//            DataResult<List<WeightedEntry.Wrapper<EntityType<?>>>> result = module.transformedCodec.parse(opsNBT, json);
//            result.result().ifPresent(list -> {
//                System.out.println("opsJSON List: " + list);
//            });
//        });


        RandomAmountModule randomAmountModule = new RandomAmountModule(1, 5);

        EntityInfoModule entityInfoModule = new EntityInfoModule(EntityType.PIG, randomAmountModule);

        DynamicOps<Tag> opsNBT = NbtOps.INSTANCE;
        DynamicOps<JsonElement> opsJSON = JsonOps.INSTANCE;

        ModuleRegistry.Codecs.ENTITY_INFO_CODEC.get().encodeStart(opsNBT, entityInfoModule).result().ifPresent(nbt -> {
            DataResult<IEntityInfoModule> parse = ModuleRegistry.Codecs.ENTITY_INFO_CODEC.get().parse(opsNBT, nbt);
            parse.result().ifPresent(module -> {
                System.out.println("opsNBT module: " + module);
            });
            System.out.println("opsNBT nbt" + nbt);
        });

//        ModAftermathModule.MODULE_CODEC.get().encodeStart(opsNBT, randomAmountModule).result().ifPresent(nbt -> {
//            DataResult<RandomAmountModule> parse = ModAftermathModule.MODULE_CODEC.get().parse(opsNBT, nbt);
//            parse.result().ifPresent(module -> {
//                System.out.println("opsNBT module: " + module);
//            });
//            System.out.println("opsNBT nbt" + nbt);
//        });




    }
}
