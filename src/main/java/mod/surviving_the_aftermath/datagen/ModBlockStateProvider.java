package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModBlocks;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(PackOutput packOutput, ExistingFileHelper exFileHelper) {
		super(packOutput, Main.MODID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {
		simpleBlockWithItem(ModBlocks.EXAMPLE.get(), cubeAll(ModBlocks.EXAMPLE.get()));
	}

}