package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModItems;
import mod.surviving_the_aftermath.util.ModCommonUtils;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.ForgeRegistries;

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
        ResourceLocation enchantedBookTexture = this.mcLoc("item/" + Items.ENCHANTED_BOOK);
        ItemModelBuilder builder0 = this.getBuilder(this.mcLoc("enchanted_book").toString());
        ItemModelBuilder builder1 = builder0.parent(new ModelFile.UncheckedModelFile("item/generated"));
        ItemModelBuilder builder2 = builder1.texture("layer0", enchantedBookTexture);
        for (Enchantment enchantment : ModCommonUtils.getKnownEnchantments()) {
            ResourceLocation key = ForgeRegistries.ENCHANTMENTS.getKey(enchantment);
            ResourceLocation texture = this.modLoc("item/" + key.getPath());
            String path = key.getNamespace() + ":enchanted_book_" + key.getPath();
            ModelFile modelFile = new ModelFile.UncheckedModelFile("item/generated");
            this.getBuilder(path).parent(modelFile).texture("layer0", texture);
        }
        for (int i = 1; i < ModCommonUtils.ENCHANTMENTS.length; i++) {
            ResourceLocation key = ForgeRegistries.ENCHANTMENTS.getKey(ModCommonUtils.ENCHANTMENTS[i].get());
            ModelFile modelFile = new ModelFile.UncheckedModelFile(this.modLoc("item/enchanted_book_" + key.getPath()));
            builder2.override().predicate(this.modLoc("special"), i / 10.0F).model(modelFile).end();
        }
    }

}