package mod.surviving_the_aftermath.init;

import com.google.common.collect.ImmutableSet;
import mod.surviving_the_aftermath.Main;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.village.poi.PoiType;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModVillagers {

    public static final ResourceKey<PoiType> RELIC_DEALER_POI = ResourceKey.create(Registries.POINT_OF_INTEREST_TYPE, Main.asResource("relic_dealer"));
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(ForgeRegistries.VILLAGER_PROFESSIONS, Main.MODID);
    public static final RegistryObject<VillagerProfession> RELIC_DEALER = VILLAGER_PROFESSIONS.register("relic_dealer", () -> new VillagerProfession(
            "relic_dealer", x -> x.is(RELIC_DEALER_POI), x -> x.is(RELIC_DEALER_POI), ImmutableSet.of(), ImmutableSet.of(), SoundEvents.VILLAGER_WORK_CLERIC));

}