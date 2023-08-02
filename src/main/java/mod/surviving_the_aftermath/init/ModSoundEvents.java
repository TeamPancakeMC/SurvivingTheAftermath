package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Main.MODID);

    public static final RegistryObject<SoundEvent> ORCHELIAS_VOX = register("orchelias_vox");
    private static RegistryObject<SoundEvent> register(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(Main.asResource(name)));
    }
}
