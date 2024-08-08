package stellanera.test.common.recipe;

import com.google.common.collect.Lists;
import net.minecraft.core.HolderLookup;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingBookCategory;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import stellanera.test.Modname;
import stellanera.test.common.block.ModBlocks;
import stellanera.test.common.datacomponent.ModComponents;
import stellanera.test.common.datacomponent.RecordFluidStack;
import stellanera.test.common.tile.SolidTile;

import java.util.List;

public class TankRecipe extends CustomRecipe {
    private List<ItemStack> stackList = Lists.newArrayList(
            new ItemStack(Items.POLISHED_DEEPSLATE), new ItemStack(Items.POLISHED_GRANITE), new ItemStack(Items.POLISHED_DEEPSLATE),
            new ItemStack(ModBlocks.TANK_ITEM.get()), ItemStack.EMPTY, new ItemStack(ModBlocks.TANK_ITEM.get()),
            new ItemStack(Items.POLISHED_DEEPSLATE), new ItemStack(Items.POLISHED_DEEPSLATE), new ItemStack(Items.POLISHED_DEEPSLATE)
    );
    public TankRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        List<ItemStack> inputList = input.items();
        if (inputList.size() != stackList.size()) {
            return false;
        }
        for (int i = 0; i < input.size(); i++) {
            if (!inputList.get(i).is(stackList.get(i).getItem())) {
                return false;
            }
        }
        Integer tank1 = inputList.get(3).get(ModComponents.TANK_TIER);
        Integer tank2 = inputList.get(5).get(ModComponents.TANK_TIER);

        return tank1 != null && tank1.equals(tank2);
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        ItemStack tank1 = input.getItem(3);
        ItemStack tank2 = input.getItem(5);
        Integer tier1 = tank1.get(ModComponents.TANK_TIER);
        Integer tier2 = tank2.get(ModComponents.TANK_TIER);
        if (tier1 == null || !tier1.equals(tier2)) {
            return ItemStack.EMPTY;
        }
        int resTier = tier1 + 1;
        ItemStack resultStack = new ItemStack(ModBlocks.TANK_ITEM, 1);
        resultStack.set(ModComponents.TANK_TIER, resTier);

        RecordFluidStack record1 = tank1.get(ModComponents.FLUID_CONTENT);
        RecordFluidStack record2 = tank2.get(ModComponents.FLUID_CONTENT);

        FluidStack fluid1 = FluidStack.EMPTY;
        if (record1 != null) {
            fluid1 = record1.stack();
        }
        FluidStack fluid2 = FluidStack.EMPTY;
        if (record2 != null) {
            fluid2 = record2.stack();
        }

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

        resultStack.set(ModComponents.FLUID_CONTENT, new RecordFluidStack(resultFluid));
        return resultStack;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return false;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return ModRecipes.TANK_SERIALIZER.get();
    }
}
