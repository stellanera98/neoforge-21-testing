package stellanera.test.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import stellanera.test.Modname;
import stellanera.test.common.block.blockitem.ARCBlock;
import stellanera.test.common.datacomponent.ModComponents;
import stellanera.test.common.datacomponent.RecordFluidStack;
import stellanera.test.common.block.blockitem.TankItem;
import stellanera.test.common.tile.SolidTile;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Modname.MODID);
    public static final DeferredRegister<Item> BLOCKITEMS = DeferredRegister.createItems(Modname.MODID);

    public static final DeferredHolder<Block, SolidBlock> SOLID = BLOCKS.register("solid", SolidBlock::new);
    public static final DeferredHolder<Block, ARCBlock> ARC = BLOCKS.register("arc", ARCBlock::new);

    public static final DeferredHolder<Item, TankItem> TANK_ITEM = BLOCKITEMS.register("tank_item", () -> new TankItem(SOLID.get(), new Item.Properties().stacksTo(1).component(ModComponents.TANK_TIER, 0)));
    public static final DeferredHolder<Item, BlockItem> ARC_ITEM = BLOCKITEMS.register("arc_item", () -> new BlockItem(ARC.get(), new Item.Properties()));

    public static void register(IEventBus modbus, IEventBus eventBus) {
        BLOCKS.register(modbus);
        BLOCKITEMS.register(modbus);
        eventBus.addListener(ModBlocks::onSolidDrop);
    }

    private static void onSolidDrop(BlockDropsEvent event) {
        BlockEntity be = event.getBlockEntity();
        if (!(be instanceof SolidTile tile)) {
            return;
        }
        FluidStack fluidStack = tile.getContents();
        int tier = tile.getTier();
        ItemStack dropStack = new ItemStack(TANK_ITEM.get(), 1);
        dropStack.set(ModComponents.TANK_TIER, tier);
        if (!fluidStack.isEmpty()) {
            dropStack.set(ModComponents.FLUID_CONTENT, new RecordFluidStack(fluidStack));
        }
        BlockPos pos = tile.getBlockPos();
        event.getDrops().clear();
        event.getDrops().add(new ItemEntity(event.getLevel(), pos.getX(), pos.getY(), pos.getZ(), dropStack));
    }
}
