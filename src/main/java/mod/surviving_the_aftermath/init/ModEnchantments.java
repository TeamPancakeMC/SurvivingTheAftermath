package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.enchantment.*;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEnchantments {

    public static final DeferredRegister<Enchantment> ENCHANTMENTS = DeferredRegister.create(ForgeRegistries.ENCHANTMENTS, Main.MODID);
    public static final RegistryObject<Enchantment> COUNTER_ATTACK = ENCHANTMENTS.register("counter_attack", CounterattackEnchantment::new);
    public static final RegistryObject<Enchantment> BLOODTHIRSTY = ENCHANTMENTS.register("bloodthirsty", BloodthirstyEnchantment::new);
    public static final RegistryObject<Enchantment> CLEAN_WATER = ENCHANTMENTS.register("clean_water", CleanWaterEnchantment::new);
    public static final RegistryObject<Enchantment> LIFE_TREE = ENCHANTMENTS.register("life_tree", LifeTreeEnchantment::new);
    public static final RegistryObject<Enchantment> DEVOURED = ENCHANTMENTS.register("devoured", DevouredEnchantment::new);
    public static final RegistryObject<Enchantment> FRANTIC = ENCHANTMENTS.register("frantic", FranticEnchantment::new);
    public static final RegistryObject<Enchantment> EXECUTE = ENCHANTMENTS.register("execute", ExecuteEnchantment::new);
    public static final RegistryObject<Enchantment> MOON = ENCHANTMENTS.register("moon", () -> new SunAndMoonEnchantment(0));
    public static final RegistryObject<Enchantment> SUN = ENCHANTMENTS.register("sun", () -> new SunAndMoonEnchantment(1));

}