package com.pancake.surviving_the_aftermath.common.data.datagen;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import com.pancake.surviving_the_aftermath.common.init.ModSoundEvents;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;

public class ModSoundProvider extends SoundDefinitionsProvider {

	protected ModSoundProvider(PackOutput packOutput, ExistingFileHelper helper) {
		super(packOutput, SurvivingTheAftermath.MOD_ID, helper);
	}

	@Override
	public void registerSounds() {
		addWithSameName(ModSoundEvents.ORCHELIAS_VOX.get(), true);
	}

	private void addWithSameName(SoundEvent sound, boolean stream) {
		add(sound, definition().with(sound(sound.getLocation()).stream(stream)));
	}
}