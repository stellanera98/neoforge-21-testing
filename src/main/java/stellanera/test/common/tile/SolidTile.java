package stellanera.test.common.tile;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.templates.FluidTank;
import stellanera.test.Modname;

public class SolidTile extends BlockEntity {
    private int counter = 0;
    private FluidTank tank = new FluidTank(16 * FluidType.BUCKET_VOLUME) {
        @Override
        protected void onContentsChanged() {
            super.onContentsChanged();
            setChanged();
        }
    };
    private int tier;

    public SolidTile(BlockPos pos, BlockState blockState) {
        this(pos, blockState, 0);
    }

    public SolidTile(BlockPos pos, BlockState state, int tier) {
        super(ModTiles.SOLID_TYPE.get(), pos, state);
        this.tier = tier;
        tank.setCapacity(getCapacityForTier(tier));
    }

    public FluidStack getContents() {
        return tank.getFluid().copy();
    }

    public void setContents(FluidStack stack) {
        tank.setFluid(stack.copy());
    }

    public int getTier() {
        return tier;
    }

    public static int getCapacityForTier(int tier) {
        double res = Math.pow(2, tier) * FluidType.BUCKET_VOLUME;
        return (int) Math.min(res, Integer.MAX_VALUE);
    }

    public static IFluidHandler getHandler(BlockEntity tile) {
        if (tile instanceof SolidTile solidTile) {
            return solidTile.tank;
        }
        return null;
    }

    @Override
    protected void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tank.readFromNBT(registries, tag);
        super.loadAdditional(tag, registries);
    }

    @Override
    protected void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        tank.writeToNBT(registries, tag);
        super.saveAdditional(tag, registries);
    }

    @Override
    public void setChanged() {
        super.setChanged();
    }

    public static <T extends BlockEntity> void tick(Level level, BlockPos blockPos, BlockState blockState, T t) {
        SolidTile tile = (SolidTile) t;
        tile.counter++;
        if (level.isClientSide) {
            return;
        }
        if (tile.counter % 20 == 0) {
            Modname.LOGGER.debug("Has: {}:{}", tile.tank.getFluid().getFluidHolder(), tile.tank.getFluidAmount());
        }
    }
}
