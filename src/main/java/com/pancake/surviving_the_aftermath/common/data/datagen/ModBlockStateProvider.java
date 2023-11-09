package com.pancake.surviving_the_aftermath.common.data.datagen;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockStateProvider extends BlockStateProvider {

	public ModBlockStateProvider(PackOutput packOutput, ExistingFileHelper exFileHelper) {
		super(packOutput, SurvivingTheAftermath.MOD_ID, exFileHelper);
	}

	@Override
	protected void registerStatesAndModels() {

	}

}