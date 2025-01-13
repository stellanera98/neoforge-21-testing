package stellanera.test.common.block.blockitem;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import stellanera.test.common.datacomponent.ModComponents;
import stellanera.test.common.tile.SolidTile;

import java.util.List;

public class TankItem extends BlockItem {
    public TankItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
        Integer tier = stack.get(ModComponents.TIER);
        if (tier == null) {
            return;
        }
        tooltipComponents.add(Component.translatable("tooltip.test.tanktier", tier, SolidTile.getCapacityForTier(tier)));
        SimpleFluidContent rfs = stack.getOrDefault(ModComponents.FLUID_CONTENT, SimpleFluidContent.EMPTY);
        FluidStack fluidStack = rfs.copy();
        if (fluidStack.isEmpty()) {
            return;
        }
        tooltipComponents.add(Component.translatable("tooltip.test.fluidamount", fluidStack.getHoverName(), fluidStack.getAmount()));
    }
}
