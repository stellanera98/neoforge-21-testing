package stellanera.datagen_test.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.client.model.generators.VariantBlockStateBuilder;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import stellanera.test.Modname;
import stellanera.test.common.block.ARCBlock;
import stellanera.test.common.block.ModBlocks;
import stellanera.test.util.EnumDemonWillType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Modname.MODID, existingFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        VariantBlockStateBuilder builder = getVariantBuilder(ModBlocks.ARC.get());
        Map<EnumDemonWillType, String> suffixMap = new HashMap<>();

        suffixMap.put(EnumDemonWillType.DEFAULT, "");
        suffixMap.put(EnumDemonWillType.CORROSIVE, "_c");
        suffixMap.put(EnumDemonWillType.VENGEFUL, "_v");
        suffixMap.put(EnumDemonWillType.DESTRUCTIVE, "_d");
        suffixMap.put(EnumDemonWillType.STEADFAST, "_s");

        builder.forAllStates(state -> {
            return ConfiguredModel.builder()
                    .modelFile(getModelFile(suffixMap.get(state.getValue(ARCBlock.TYPE)), state.getValue(ARCBlock.LIT)))
                    .rotationY((int) state.getValue(ARCBlock.FACING).toYRot())
                    .build();
        });
    }

    private ModelFile getModelFile(String suffix, boolean lit) {
        ResourceLocation bottom = modLoc("block/arc_bottom");
        ResourceLocation side = modLoc("block/arc_side" + suffix + (lit ? "_lit" : ""));
        ResourceLocation front = modLoc("block/arc_front" + suffix + (lit ? "_lit" : ""));
        ResourceLocation top = modLoc("block/arc_top" + suffix);
        String name = "arc" + suffix + (lit? "_lit" : "");

        return models().orientableWithBottom(name, side, front, bottom, top);
    }
}
