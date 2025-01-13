package stellanera.test.common.recipe;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeInput;
import net.neoforged.neoforge.fluids.FluidStack;

public record MythingRecipeInput(ItemStack toolStack, ItemStack inputStack, FluidStack inputFluidStack) implements RecipeInput {

    @Override
    public ItemStack getItem(int index) {
        return switch (index) {
            case 0 -> toolStack();
            case 1 -> inputStack();
            default -> throw new IllegalArgumentException("No item for index {}");
        };
    }

    @Override
    public int size() {
        return 2;
    }
}
