package mod.surviving_the_aftermath.init;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.structure.CityStructure;
import mod.surviving_the_aftermath.structure.HouseOfSakura;
import mod.surviving_the_aftermath.structure.NetherRaidStructure;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModStructureTypes {
	public static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES = DeferredRegister
			.create(Registries.STRUCTURE_TYPE.location(), Main.MODID);

	public static final RegistryObject<StructureType<?>> CITY = STRUCTURE_TYPES.register("city",
			() -> () -> CityStructure.CODEC);
	public static final RegistryObject<StructureType<?>> HOUSE_OF_SAKURA = STRUCTURE_TYPES.register("house_of_sakura",
			() -> () -> HouseOfSakura.CODEC);
	public static final RegistryObject<StructureType<?>> NETHER_RAID = STRUCTURE_TYPES.register("nether_raid",
			() -> () -> NetherRaidStructure.CODEC);
}
