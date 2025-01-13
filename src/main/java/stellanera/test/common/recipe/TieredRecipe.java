package stellanera.test.common.recipe;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import stellanera.test.Modname;
import stellanera.test.common.block.ModBlocks;
import stellanera.test.common.datacomponent.ModComponents;
import stellanera.test.common.tile.SolidTile;

import java.util.List;

public class TieredRecipe extends CustomRecipe {

    private final ShapedRecipePattern pattern;
    private final ItemStack tieredStack;
    private final int primary;
    private final int secondary;
    public TieredRecipe(ShapedRecipePattern shapedRecipePattern, ItemStack resultStack, int primary, int secondary) {
        super(CraftingBookCategory.MISC);
        this.pattern = shapedRecipePattern;
        this.tieredStack = resultStack;
        this.primary = primary;
        this.secondary = secondary;
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        if (!pattern.matches(input)) {
            return false;
        }

        ItemStack stack1 = input.getItem(primary);
        ItemStack stack2 = input.getItem(secondary);
        int tier1 = stack1.getOrDefault(ModComponents.TIER, -1);
        int tier2 = stack2.getOrDefault(ModComponents.TIER, -1);
        return tier1 == tier2 && tier1 != -1;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack tank1 = input.getItem(primary);
        ItemStack tank2 = input.getItem(secondary);
        int tier1 = tank1.getOrDefault(ModComponents.TIER, -1);
        int tier2 = tank2.getOrDefault(ModComponents.TIER, -1);
        if (!(tier1 == tier2)) {
            return ItemStack.EMPTY;
        }
        int resTier = tier1 + 1;
        ItemStack resultStack = tieredStack.copy();
        resultStack.set(ModComponents.TIER, resTier);

        if (tank1.has(ModComponents.FLUID_CONTENT)) {
            SimpleFluidContent content1 = tank1.getOrDefault(ModComponents.FLUID_CONTENT, SimpleFluidContent.EMPTY);
            SimpleFluidContent content2 = tank2.getOrDefault(ModComponents.FLUID_CONTENT, SimpleFluidContent.EMPTY);

            FluidStack fluid1 = content1.copy();
            FluidStack fluid2 = content2.copy();

            if (fluid1.isEmpty() && fluid2.isEmpty()) {
                return resultStack;
            }

            FluidStack resultFluid;
            long fluidAmount;
            if (!fluid1.isEmpty()) {
                if (!fluid2.isEmpty()) {
                    if (fluid1.is(fluid2.getFluid())) {
                        // both are not empty and the same
                        resultFluid = fluid1.copy();
                        // need to cast both to long here otherwise its still doing the int overflow thing
                        fluidAmount = (long) fluid1.getAmount() + (long) fluid2.getAmount();
                    } else {
                        // both are not empty but different
                        resultFluid = fluid1.copy();
                        fluidAmount = fluid1.getAmount();
                    }
                } else {
                    // 1 has fluid, 2 has not
                    resultFluid = fluid1.copy();
                    fluidAmount = fluid1.getAmount();
                }
            } else {
                // 1 has no fluid, 2 has (as they cant both be empty as per previous if)
                resultFluid = fluid2.copy();
                fluidAmount = fluid2.getAmount();
            }

            resultFluid.setAmount((int) Math.min(fluidAmount, SolidTile.getCapacityForTier(resTier)));

            resultStack.set(ModComponents.FLUID_CONTENT, SimpleFluidContent.copyOf(resultFluid));
        }

        if (tank1.has(ModComponents.ENERGY_CONTENT)) {
            int content1 = tank1.getOrDefault(ModComponents.ENERGY_CONTENT, 0);
            int content2 = tank2.getOrDefault(ModComponents.ENERGY_CONTENT, 0);

            int res = Math.clamp((long) content1 + (long) content2, 0, SolidTile.getCapacityForTier(resTier));

            resultStack.set(ModComponents.ENERGY_CONTENT, res);
        }
        return resultStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return pattern.width() <= width && pattern.height() <= height;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.TIERED_RECIPE_SERIALIZER.get();
    }

    public static class Serializer implements RecipeSerializer<TieredRecipe> {
        public static final MapCodec<TieredRecipe> CODEC = RecordCodecBuilder.mapCodec(
                codecBuilder -> codecBuilder.group(
                                ShapedRecipePattern.MAP_CODEC.forGetter(recipe -> recipe.pattern),
                                ItemStack.STRICT_CODEC.fieldOf("result").forGetter(recipe -> recipe.tieredStack),
                                Codec.INT.fieldOf("primary").forGetter(recipe -> recipe.primary),
                                Codec.INT.fieldOf("secondary").forGetter(recipe -> recipe.secondary)
                        )
                        .apply(codecBuilder, TieredRecipe::new)
        );
        public static final StreamCodec<RegistryFriendlyByteBuf, TieredRecipe> STREAM_CODEC = StreamCodec.of(
                TieredRecipe.Serializer::toNetwork, TieredRecipe.Serializer::fromNetwork
        );


        @Override
        public MapCodec<TieredRecipe> codec() {
            return CODEC;
        }

        @Override
        public StreamCodec<RegistryFriendlyByteBuf, TieredRecipe> streamCodec() {
            return STREAM_CODEC;
        }

        private static TieredRecipe fromNetwork(RegistryFriendlyByteBuf buffer) {
            ShapedRecipePattern shapedrecipepattern = ShapedRecipePattern.STREAM_CODEC.decode(buffer);
            ItemStack itemstack = ItemStack.STREAM_CODEC.decode(buffer);
            return new TieredRecipe(shapedrecipepattern, itemstack, buffer.readInt(), buffer.readInt());
        }

        private static void toNetwork(RegistryFriendlyByteBuf buffer, TieredRecipe recipe) {
            ShapedRecipePattern.STREAM_CODEC.encode(buffer, recipe.pattern);
            ItemStack.STREAM_CODEC.encode(buffer, recipe.tieredStack);
            buffer.writeInt(recipe.primary);
            buffer.writeInt(recipe.secondary);
        }
    }
}
