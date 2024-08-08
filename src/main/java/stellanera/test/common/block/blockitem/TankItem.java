package stellanera.test.common.block.blockitem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;
import stellanera.test.common.datacomponent.ModComponents;
import stellanera.test.common.datacomponent.RecordFluidStack;
import stellanera.test.common.tile.SolidTile;

import java.util.List;

public class TankItem extends BlockItem {
    public TankItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        Integer tier = stack.get(ModComponents.TANK_TIER);
        if (tier == null) {
            return;
        }
        tooltipComponents.add(Component.translatable("tooltip.test.tanktier", tier, SolidTile.getCapacityForTier(tier)));
        RecordFluidStack rfs = stack.getOrDefault(ModComponents.FLUID_CONTENT, new RecordFluidStack(FluidStack.EMPTY));
        if (rfs.stack().isEmpty()) {
            return;
        }
        tooltipComponents.add(Component.translatable("tooltip.test.fluidamount", rfs.stack().getHoverName(), rfs.stack().getAmount()));
    }
}
