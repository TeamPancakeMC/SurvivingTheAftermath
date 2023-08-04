package mod.surviving_the_aftermath.datagen;

import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

import java.util.Map;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Main.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.basicItem(ModItems.RAW_FALUKORV.get());
        this.basicItem(ModItems.COOKED_FALUKORV.get());
        this.basicItem(ModItems.EGG_TART.get());
        this.basicItem(ModItems.STACK_OF_EGG_TARTS.get());
        this.basicItem(ModItems.HAMBURGER.get());
        this.basicItem(ModItems.TIANJIN_PANCAKE.get());
        this.basicItem(ModItems.NETHER_CORE.get());
        this.getBuilder(ModItems.MUSIC_DISK_ORCHELIAS_VOX.getId().toString())
                .parent(new ModelFile.UncheckedModelFile("item/template_music_disc"))
                .texture("layer0", this.modLoc("item/music_disk_orchelias_vox"));
        for (Map.Entry<String, Float> entry : ENCHANTMENTS.entrySet()) {
            getBuilder("minecraft:item/enchanted_book")
                    .parent(new ModelFile.UncheckedModelFile("item/generated"))
                    .texture("layer0", mcLoc("item/enchanted_book")).override()
                    .predicate(modLoc("special"), entry.getValue())
                    .model(generateModel(entry.getKey().split("\\.")[2]))
                    .end();
        }
    }

    public ModelFile generateModel(String name) {
        ResourceLocation location = modLoc("item/" + name);
        if (!existingFileHelper.exists(location, PackType.CLIENT_RESOURCES, ".png", "textures")) {
            location = mcLoc("item/enchanted_book");
        }

        return getBuilder(name).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", location);
    }

    public static Map<String, Float> ENCHANTMENTS = new Object2FloatOpenHashMap<>();

    static {
        String e = "enchantment." + Main.MODID + ".";
        ENCHANTMENTS.put(e + "counter_attack", 0.1F);
        ENCHANTMENTS.put(e + "bloodthirsty", 0.2F);
        ENCHANTMENTS.put(e + "clean_water", 0.3F);
        ENCHANTMENTS.put(e + "life_tree", 0.4F);
        ENCHANTMENTS.put(e + "devoured", 0.5F);
        ENCHANTMENTS.put(e + "frantic", 0.6F);
        ENCHANTMENTS.put(e + "execute", 0.7F);
        ENCHANTMENTS.put(e + "moon", 0.8F);
        ENCHANTMENTS.put(e + "sun", 0.9F);
    }
}