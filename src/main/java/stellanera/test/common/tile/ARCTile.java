package stellanera.test.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import net.neoforged.neoforge.items.IItemHandler;
import net.neoforged.neoforge.items.ItemStackHandler;
import net.neoforged.neoforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.Nullable;
import stellanera.test.common.item.ModItems;
import stellanera.test.common.menu.ARCMenu;

public class ARCTile extends BlockEntity implements MenuProvider {

    public static final int TOOL_SLOT = 0;
    public static final int INPUT_SLOT = 1;
    public static final int INPUT_BUCKET_SLOT = 2;
    public static final int OUTPUT_BUCKET_SLOT = 3;
    public static final int OUTPUT_SLOT = 4;

    public static final int NUM_OUTPUTS = 5;

    public ARCTile(BlockPos pos, BlockState blockState) {
        super(ModTiles.ARC_TYPE.get(), pos, blockState);
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(8) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
        }

        @Override
        public boolean isItemValid(int slot, ItemStack stack) {
            return switch (slot) {
                case TOOL_SLOT -> stack.is(ModItems.ARC_TOOL_MYTHING);
                case INPUT_BUCKET_SLOT, OUTPUT_BUCKET_SLOT -> FluidUtil.getFluidHandler(stack).isPresent();
                case INPUT_SLOT -> true;
                default -> false;
            };
        }
    };

    public static IItemHandler getItemHandler(ARCTile tile, @Nullable Direction side) {
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
        inputTank.readFromNBT(registries, tag);
        outputTank.readFromNBT(registries, tag);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        CompoundTag inv = itemHandler.serializeNBT(registries);
        tag.put("arcinv", inv);
        inputTank.writeToNBT(registries, tag);
        outputTank.writeToNBT(registries, tag);
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory playerInventory, Player player) {
        return new ARCMenu(containerId, playerInventory, this);
    }
}
