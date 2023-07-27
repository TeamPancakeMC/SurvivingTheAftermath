package mod.surviving_the_aftermath.datagen;

import mod.surviving_the_aftermath.Main;
import mod.surviving_the_aftermath.init.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

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
    }

}