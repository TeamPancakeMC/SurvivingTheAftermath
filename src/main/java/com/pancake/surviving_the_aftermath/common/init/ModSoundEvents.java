package com.pancake.surviving_the_aftermath.common.init;

import com.pancake.surviving_the_aftermath.SurvivingTheAftermath;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, SurvivingTheAftermath.MOD_ID);

    public static final RegistryObject<SoundEvent> ORCHELIAS_VOX = register("orchelias_vox");
    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(SurvivingTheAftermath.asResource(name)));
    }
}
