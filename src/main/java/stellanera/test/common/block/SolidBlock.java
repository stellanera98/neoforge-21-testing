package stellanera.test.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import stellanera.test.common.datacomponent.ModComponents;
import stellanera.test.common.datacomponent.RecordFluidStack;
import stellanera.test.common.tile.ModTiles;
import stellanera.test.common.tile.SolidTile;


public class SolidBlock extends Block implements EntityBlock {

    public static final VoxelShape BOX = Block.box(0.25 * 16, 0, 0.25 * 16, 0.75 * 16, 0.8 * 16, 0.75 * 16);
    private int tier = 0;
    public SolidBlock() {
        super(Properties.of().lightLevel(state -> 9).forceSolidOn());
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> blockEntityType) {
        return blockEntityType == ModTiles.SOLID_TYPE.get() ? SolidTile::tick : null;
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
        return new SolidTile(pos, state, tier);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        Integer tier = stack.get(ModComponents.TANK_TIER);
        if (tier == null) {
            return;
        }
        this.tier = tier;
        BlockEntity be = level.getBlockEntity(pos);
        RecordFluidStack rfs = stack.getOrDefault(ModComponents.FLUID_CONTENT, new RecordFluidStack(FluidStack.EMPTY));
        if (!(be instanceof SolidTile tile) || rfs.stack().isEmpty()) {
            return;
        }
        tile.setContents(rfs.stack());
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOX;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        IFluidHandler tank = level.getCapability(Capabilities.FluidHandler.BLOCK, pos, null);
        if (tank == null) {
            return ItemInteractionResult.SKIP_DEFAULT_BLOCK_INTERACTION;
        }
        if (FluidUtil.interactWithFluidHandler(player, hand, tank)) {
            player.getInventory().setChanged();
            return ItemInteractionResult.sidedSuccess(level.isClientSide);
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }
}
