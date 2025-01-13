package stellanera.test.common.recipe;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.crafting.SizedFluidIngredient;
import org.jetbrains.annotations.Nullable;

public class MythingRecipe implements Recipe<MythingRecipeInput> {

    private final Ingredient toolStack;
    private final Ingredient inputStack;
    private final SizedFluidIngredient fluidInput;
    private final ItemStack resultStack;
    private final FluidStack resultFluid;
    private final boolean hasFluidInput;

    public MythingRecipe(Ingredient toolStack, Ingredient inputStack, @Nullable SizedFluidIngredient fluidInput, ItemStack result, FluidStack outputFluid) {
        this.toolStack = toolStack;
        this.inputStack = inputStack;
        this.fluidInput = fluidInput;
        this.resultFluid = outputFluid;
        this.resultStack = result;
        this.hasFluidInput = fluidInput != null;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        NonNullList<Ingredient> list = NonNullList.create();
        list.add(toolStack);
        list.add(inputStack);

        return list;
    }

    @Override
    public boolean matches(MythingRecipeInput input, Level level) {
        if (!this.toolStack.test(input.toolStack())) {
            return false;
        }

        if (!this.inputStack.test(input.inputStack())) {
            return false;
        }

        if (this.hasFluidInput && this.fluidInput.test(input.inputFluidStack())) {return false;}
        return true;
    }

    @Override
    public ItemStack assemble(MythingRecipeInput mythingRecipeInput, HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public boolean canCraftInDimensions(int i, int i1) {
        return false;
    }

    @Override
    public ItemStack getResultItem(HolderLookup.Provider provider) {
        return null;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return null;
    }

    @Override
    public RecipeType<?> getType() {
        return null;
    }
}
