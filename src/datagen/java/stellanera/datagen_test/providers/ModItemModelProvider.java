package stellanera.datagen_test.providers;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import stellanera.test.Modname;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Modname.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {

    }
}
