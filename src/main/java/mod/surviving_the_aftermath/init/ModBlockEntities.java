package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.blockentity.ExampleBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
	public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister
			.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Main.MODID);

	public static final RegistryObject<BlockEntityType<ExampleBlockEntity>> EXAMPLE = BLOCK_ENTITIES.register("example",
			() -> BlockEntityType.Builder.<ExampleBlockEntity>of(ExampleBlockEntity::new, ModBlocks.EXAMPLE.get())
					.build(null));
}
