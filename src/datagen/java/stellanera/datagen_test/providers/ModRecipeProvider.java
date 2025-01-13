package stellanera.datagen_test.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.neoforge.common.Tags;
import stellanera.datagen_test.builder.TieredRecipeBuilder;
import stellanera.test.Modname;
import stellanera.test.common.block.ModBlocks;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TANK_ITEM.get())
                .pattern("dgd")
                .pattern("t t")
                .pattern("ddd")
                .define('d', Blocks.POLISHED_DEEPSLATE)
                .define('g', Blocks.POLISHED_GRANITE)
                .define('t', Tags.Items.GLASS_BLOCKS)
                .unlockedBy("has_polished_deepslate", has(Blocks.POLISHED_DEEPSLATE))
                .save(output, Modname.rl("tank_initial"));

        TieredRecipeBuilder.shaped(RecipeCategory.MISC, ModBlocks.TANK_ITEM.get(), 3, 5)
                .pattern("dgd")
                .pattern("t t")
                .pattern("ddd")
                .define('d', Blocks.POLISHED_DEEPSLATE)
                .define('g', Blocks.POLISHED_GRANITE)
                .define('t', ModBlocks.TANK_ITEM.get())
                .unlockedBy("has_tank", has(ModBlocks.TANK_ITEM.get()))
                .save(output, Modname.rl("tank_combine"));
    }
}
