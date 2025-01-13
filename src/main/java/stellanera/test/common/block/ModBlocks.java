package stellanera.test.common.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import stellanera.test.Modname;
import stellanera.test.common.datacomponent.ModComponents;
import stellanera.test.common.block.blockitem.TankItem;

public class ModBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.createBlocks(Modname.MODID);
    public static final DeferredRegister<Item> BLOCKITEMS = DeferredRegister.createItems(Modname.MODID);

    public static final DeferredHolder<Block, SolidBlock> SOLID = BLOCKS.register("solid", SolidBlock::new);
    public static final DeferredHolder<Block, ARCBlock> ARC = BLOCKS.register("arc", ARCBlock::new);

    public static final DeferredHolder<Item, TankItem> TANK_ITEM = BLOCKITEMS.register("tank_item", () -> new TankItem(SOLID.get(), new Item.Properties().stacksTo(1).component(ModComponents.TIER, 0)));
    public static final DeferredHolder<Item, BlockItem> ARC_ITEM = BLOCKITEMS.register("arc_item", () -> new BlockItem(ARC.get(), new Item.Properties()));

    public static void register(IEventBus modbus, IEventBus eventBus) {
        BLOCKS.register(modbus);
        BLOCKITEMS.register(modbus);
    }
}
