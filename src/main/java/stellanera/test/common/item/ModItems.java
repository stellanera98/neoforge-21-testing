package stellanera.test.common.item;

import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import stellanera.test.Modname;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(Modname.MODID);

    public static final DeferredHolder<Item, Item> ARC_TOOL_MYTHING = ITEMS.registerItem("arc_tool_mything", Item::new, new Item.Properties().durability(64));
    public static final DeferredHolder<Item, Item> ARC_TOOL_BLASTING = ITEMS.registerItem("arc_tool_blasting", Item::new, new Item.Properties().durability(64));
    public static final DeferredHolder<Item, Item> ARC_TOOL_FURNACE = ITEMS.registerItem("arc_tool_furnace", Item::new, new Item.Properties().durability(64));

    public static final DeferredHolder<Item, Item> PRIMITIVE_ARC_TOOL_MYTHING = ITEMS.registerItem("primitive_arc_tool_mything", Item::new, new Item.Properties().durability(256));
    public static final DeferredHolder<Item, Item> PRIMITIVE_ARC_TOOL_BLASTING = ITEMS.registerItem("primitive_arc_tool_blasting", Item::new, new Item.Properties().durability(256));
    public static final DeferredHolder<Item, Item> PRIMITIVE_ARC_TOOL_FURNACE = ITEMS.registerItem("primitive_arc_tool_furnace", Item::new, new Item.Properties().durability(256));

    public static final DeferredHolder<Item, Item> HELLFORGED_ARC_TOOL_MYTHING = ITEMS.registerItem("hellforged_arc_tool_mything", Item::new, new Item.Properties().durability(1024));
    public static final DeferredHolder<Item, Item> HELLFORGED_ARC_TOOL_BLASTING = ITEMS.registerItem("hellforged_arc_tool_blasting", Item::new, new Item.Properties().durability(1024));
    public static final DeferredHolder<Item, Item> HELLFORGED_ARC_TOOL_FURNACE = ITEMS.registerItem("hellforged_arc_tool_furnace", Item::new, new Item.Properties().durability(1024));

    public static void register(IEventBus modbus) {
        ITEMS.register(modbus);
    }
}
