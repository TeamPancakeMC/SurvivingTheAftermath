package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.MobEffect.CowardiceEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Main.MODID);

    //懦弱
    public static final RegistryObject<MobEffect> COWARDICE = MOB_EFFECTS.register("cowardice", CowardiceEffect::new);

}
