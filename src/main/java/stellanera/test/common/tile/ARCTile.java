package stellanera.test.common.tile;

import net.minecraft.client.gui.screens.inventory.FurnaceScreen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.FurnaceMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandler.FluidAction;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import stellanera.test.Modname;
import stellanera.test.common.block.ARCBlock;
import stellanera.test.common.menu.ARCMenu;
import stellanera.test.common.tag.ModTags;
import stellanera.test.util.ARCOutputHandler;
import stellanera.test.util.EnumDemonWillType;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ARCTile extends BlockEntity implements MenuProvider {

    public static final int TOOL_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int INPUT_BUCKET_SLOT = 2;
    public static final int OUTPUT_BUCKET_SLOT = 3;
    public static final int OUTPUT_SLOT = 4;

    public static final int NUM_OUTPUTS = 5;

    private double progress = 0;
    public static final double DEFAULT_SPEED = 0.005;

    public ARCTile(BlockPos pos, BlockState blockState) {
        super(ModTiles.ARC_TYPE.get(), pos, blockState);
        //Modname.LOGGER.debug("tile created at {}", pos);
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(OUTPUT_SLOT + NUM_OUTPUTS) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case TOOL_SLOT -> stack.is(ModTags.Items.ARC_TOOL);
                case INPUT_BUCKET_SLOT, OUTPUT_BUCKET_SLOT -> FluidUtil.getFluidHandler(stack).isPresent();
                case INPUT_SLOT -> true;
                default -> false;
            };
        }

        @Override
        public int getSlotLimit(int slot) {
            if (slot == INPUT_BUCKET_SLOT || slot == OUTPUT_BUCKET_SLOT) {
                return 1;
            }
            return super.getSlotLimit(slot);
        }
    };

    public int getProgressForGui() {
        return (int) (progress * 38);
    }

    public static IItemHandler getItemHandler(ARCTile tile, @Nullable Direction side) {
        if (side == null) {
            return tile.itemHandler;
        }
        switch (side) {
            case UP:
                return new RangedWrapper(tile.itemHandler, TOOL_SLOT, TOOL_SLOT + 1);
            case DOWN:
                new RangedWrapper(tile.itemHandler, OUTPUT_SLOT, OUTPUT_SLOT + NUM_OUTPUTS);
            default:
                return new RangedWrapper(tile.itemHandler, INPUT_SLOT, OUTPUT_BUCKET_SLOT+1);
        }
    }

    private final FluidTank inputTank = new FluidTank(20 * FluidType.BUCKET_VOLUME) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    private final FluidTank outputTank = new FluidTank(20 * FluidType.BUCKET_VOLUME) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }
    };

    @Override
    public Component getDisplayName() {
        return Component.literal("Another Reinterpretation Chlaui");
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        CompoundTag inv = tag.getCompound("arcinv");
        itemHandler.deserializeNBT(registries, inv);
        inputTank.readFromNBT(registries, tag.getCompound("inputtank"));
        outputTank.readFromNBT(registries, tag.getCompound("outputtank"));
        progress = tag.getDouble("arcprogress");
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag inv = itemHandler.serializeNBT(registries);
        tag.put("arcinv", inv);
        CompoundTag input = new CompoundTag();
        CompoundTag output = new CompoundTag();
        inputTank.writeToNBT(registries, input);
        outputTank.writeToNBT(registries, output);
        tag.put("inputtank", input);
        tag.put("outputtank", output);
        tag.putDouble("arcprogress", progress);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ARCMenu(containerId, playerInventory, this);
    }

    public IFluidHandler getFluidHandler(Direction direction) {
        if (direction == Direction.DOWN) {
            return this.outputTank;
        }
        return this.inputTank;
    }

    public static EnumDemonWillType getTypeForTool(ItemStack toolStack) {
        EnumDemonWillType type = EnumDemonWillType.DEFAULT;
        if (toolStack.is(ModTags.Items.ARC_TOOL_SMOKING)) {
            type = EnumDemonWillType.CORROSIVE;
        }
        if (toolStack.is(ModTags.Items.ARC_TOOL_FURNACE)) {
            type = EnumDemonWillType.VENGEFUL;
        }
        if (toolStack.is(ModTags.Items.ARC_TOOL_BLASTING)) {
            type = EnumDemonWillType.DESTRUCTIVE;
        }
        if (toolStack.is(ModTags.Items.ARC_TOOL_MYTHING)) {
            type = EnumDemonWillType.STEADFAST;
        }

        return type;
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState state, T tile) {
        if (!(tile instanceof ARCTile arcTile)) {
            return;
        }
        ItemStack[] outputItems = {
                arcTile.itemHandler.getStackInSlot(OUTPUT_SLOT),
                arcTile.itemHandler.getStackInSlot(OUTPUT_SLOT + 1),
                arcTile.itemHandler.getStackInSlot(OUTPUT_SLOT + 2),
                arcTile.itemHandler.getStackInSlot(OUTPUT_SLOT + 3),
                arcTile.itemHandler.getStackInSlot(OUTPUT_SLOT + 4)
        };
        ARCOutputHandler itemOutputHandler = new ARCOutputHandler(outputItems, 64);
        boolean outputChanged = false;
        if (!level.isClientSide) {
            outputChanged = arcTile.handleSlots(itemOutputHandler);
        }
        ItemStack toolStack = arcTile.itemHandler.getStackInSlot(TOOL_SLOT);
        // handle type
        EnumDemonWillType type = getTypeForTool(toolStack);
        if (state.getValue(ARCBlock.TYPE) != type) {
            level.setBlock(blockPos, state.setValue(ARCBlock.TYPE, type), Block.UPDATE_CLIENTS);
        }
        Recipe recipe = arcTile.getRecipe();
        if (arcTile.canCraft(recipe, itemOutputHandler)) {
            // set lit
            if (!(state.getValue(ARCBlock.LIT))) {
                level.setBlock(blockPos, state.setValue(ARCBlock.LIT, true), Block.UPDATE_CLIENTS);
            }
            arcTile.progress += DEFAULT_SPEED;
            if (arcTile.progress >= 1) {
                outputChanged = arcTile.craft(recipe, itemOutputHandler)|| outputChanged;
                arcTile.progress = 0;
            }
        } else {
            arcTile.progress = 0;
            if (state.getValue(ARCBlock.LIT)) {
                level.setBlock(blockPos, state.setValue(ARCBlock.LIT, false), Block.UPDATE_CLIENTS);
            }
        }

        if (outputChanged && !level.isClientSide) {
            for (int i = 0; i < NUM_OUTPUTS; i++) {
                arcTile.itemHandler.setStackInSlot(OUTPUT_SLOT + i, itemOutputHandler.getStackInSlot(i));
            }
        }
    }

    public boolean canCraft(Recipe recipe, ARCOutputHandler outputHandler) {
        if (recipe == null) {
            return false;
        }
        ItemStack toolStack = itemHandler.getStackInSlot(TOOL_SLOT).copy();
        if (toolStack.getDamageValue() >= toolStack.getMaxDamage()) {
            return false;
        }
        ItemStack output = recipe.assemble(new SingleRecipeInput(itemHandler.getStackInSlot(INPUT_SLOT).copy()), level.registryAccess());
        if (output.isEmpty()) {
            return false;
        }
        List<ItemStack> list = new ArrayList<>();
        list.add(output);
        if (!outputHandler.canTransferAllItemsToSlots(list, true)) {
            return false;
        }

        return true;
    }

    public Recipe getRecipe() {
        ItemStack toolStack = itemHandler.getStackInSlot(TOOL_SLOT);
        RecipeManager recMan = level.getRecipeManager();
        RecipeType recipeType = null;
        SingleRecipeInput input = new SingleRecipeInput(itemHandler.getStackInSlot(INPUT_SLOT).copy());
        if (toolStack.is(ModTags.Items.ARC_TOOL_FURNACE)) {
            recipeType = RecipeType.SMELTING;
        }
        if (toolStack.is(ModTags.Items.ARC_TOOL_BLASTING)) {
            recipeType = RecipeType.BLASTING;
        }
        if (toolStack.is(ModTags.Items.ARC_TOOL_SMOKING)) {
            recipeType = RecipeType.SMOKING;
        }
        if (recipeType == null) {
            return null;
        }
        Optional<RecipeHolder<?>> optional = recMan.getRecipeFor(recipeType, input, level);
        return optional.map(RecipeHolder::value).orElse(null);
    }

    public boolean craft(Recipe recipe, ARCOutputHandler outputHandler) {
        ItemStack output = recipe.assemble(new SingleRecipeInput(itemHandler.getStackInSlot(INPUT_SLOT).copy()), level.registryAccess());
        if (output.isEmpty()) {
            return false;
        }
        List<ItemStack> list = new ArrayList<>();
        list.add(output);
        if (outputHandler.canTransferAllItemsToSlots(list, true)) {
            outputHandler.canTransferAllItemsToSlots(list, false);
            itemHandler.getStackInSlot(TOOL_SLOT).setDamageValue(itemHandler.getStackInSlot(TOOL_SLOT).getDamageValue() + 1);
            itemHandler.getStackInSlot(INPUT_SLOT).shrink(1);
            return true;
        }
        return false;
    }

    public boolean handleSlots(ARCOutputHandler itemOutputHandler) {
        IFluidHandlerItem testInputHandler = FluidUtil.getFluidHandler(itemHandler.getStackInSlot(INPUT_BUCKET_SLOT).copy()).orElse(null);
        IFluidHandlerItem testOutputHandler = FluidUtil.getFluidHandler(itemHandler.getStackInSlot(OUTPUT_BUCKET_SLOT).copy()).orElse(null);

        boolean outputChanged = false;
        if (testInputHandler != null) {
            FluidStack transferredStack = FluidUtil.tryFluidTransfer(inputTank, testInputHandler, Integer.MAX_VALUE, false);
            if (!transferredStack.isEmpty()) {
                testInputHandler.drain(transferredStack, FluidAction.EXECUTE);
                List<ItemStack> arraylist = new ArrayList<>();
                arraylist.add(testInputHandler.getContainer());
                if (itemOutputHandler.canTransferAllItemsToSlots(arraylist, true)) {
                    outputChanged = true;
                    inputTank.fill(transferredStack, FluidAction.EXECUTE);
                    itemOutputHandler.canTransferAllItemsToSlots(arraylist, false);
                    itemHandler.setStackInSlot(INPUT_BUCKET_SLOT, ItemStack.EMPTY);
                }
            } else {
                transferredStack = FluidUtil.tryFluidTransfer(testInputHandler, inputTank, inputTank.getFluidAmount(), false);
                if (!transferredStack.isEmpty()) {
                    testInputHandler.fill(transferredStack, FluidAction.EXECUTE);
                    List<ItemStack> arrayList = new ArrayList<>();
                    arrayList.add(testInputHandler.getContainer());
                    if (itemOutputHandler.canTransferAllItemsToSlots(arrayList, true)) {
                        outputChanged = true;
                        inputTank.drain(transferredStack, FluidAction.EXECUTE);
                        itemOutputHandler.canTransferAllItemsToSlots(arrayList, false);
                        itemHandler.setStackInSlot(INPUT_BUCKET_SLOT, ItemStack.EMPTY);
                    }
                }
            }
        }

        if (testOutputHandler != null) {
            /* probably dont insert into output tank
            FluidStack transferredStack = FluidUtil.tryFluidTransfer(outputTank, testOutputHandler, outputTank.getCapacity() - outputTank.getFluidAmount(), false);
            if (!transferredStack.isEmpty()) {
                testOutputHandler.drain(transferredStack, FluidAction.EXECUTE);
                List<ItemStack> arraylist = new ArrayList<>();
                arraylist.add(testOutputHandler.getContainer());
                if (itemOutputHandler.canTransferAllItemsToSlots(arraylist, true)) {
                    outputChanged = true;
                    outputTank.fill(transferredStack, FluidAction.EXECUTE);
                    itemOutputHandler.canTransferAllItemsToSlots(arraylist, false);
                    itemHandler.setStackInSlot(OUTPUT_BUCKET_SLOT, ItemStack.EMPTY);
                }
            } else {

             */
            FluidStack transferredStack = FluidUtil.tryFluidTransfer(testOutputHandler, outputTank, outputTank.getFluidAmount(), false);
            if (!transferredStack.isEmpty()) {
                testOutputHandler.fill(transferredStack, FluidAction.EXECUTE);
                List<ItemStack> arrayList = new ArrayList<>();
                arrayList.add(testOutputHandler.getContainer());
                if (itemOutputHandler.canTransferAllItemsToSlots(arrayList, true)) {
                    outputChanged = true;
                    outputTank.drain(transferredStack, FluidAction.EXECUTE);
                    itemOutputHandler.canTransferAllItemsToSlots(arrayList, false);
                    itemHandler.setStackInSlot(OUTPUT_BUCKET_SLOT, ItemStack.EMPTY);
                }
            }
            //}
        }

        ItemStack toolStack = itemHandler.getStackInSlot(TOOL_SLOT).copy();
        if (toolStack.getDamageValue() >= toolStack.getMaxDamage()) {
            List<ItemStack> arrayList = new ArrayList<>();
            toolStack.setDamageValue(toolStack.getMaxDamage());
            arrayList.add(toolStack);
            if (itemOutputHandler.canTransferAllItemsToSlots(arrayList, true)) {
                outputChanged = true;
                itemOutputHandler.canTransferAllItemsToSlots(arrayList, false);
                itemHandler.setStackInSlot(TOOL_SLOT, ItemStack.EMPTY);
            }
        }

        return outputChanged;
    }
}
