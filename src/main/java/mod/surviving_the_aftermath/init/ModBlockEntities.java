package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.blockentity.ExampleBlockEntity;
import mod.surviving_the_aftermath.blockentity.PartyBonfireBlockEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);

	public static final RegistryObject<BlockEntityType<ExampleBlockEntity>> EXAMPLE = BLOCK_ENTITIES.register("example",
			() -> BlockEntityType.Builder.<ExampleBlockEntity>of(ExampleBlockEntity::new, ModBlocks.EXAMPLE.get())
					.build(null));


	public static final RegistryObject<BlockEntityType<PartyBonfireBlockEntity>> PARTY_BONFIRE = register("party_bonfire", PartyBonfireBlockEntity::new, () -> new Block[] { ModBlocks.PARTY_BONFIRE.get() });


	private static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> register(String name, BlockEntityType.BlockEntitySupplier<T> tile, Supplier<Block[]> blocks) {
		return BLOCK_ENTITIES.register(name, () -> BlockEntityType.Builder.of(tile, blocks.get()).build(null));
	}
}
