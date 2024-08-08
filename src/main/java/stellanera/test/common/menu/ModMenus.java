package stellanera.test.common.menu;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import stellanera.test.Modname;

public class ModMenus {
    public static final DeferredRegister<MenuType<?>> MENUS = DeferredRegister.create(BuiltInRegistries.MENU, Modname.MODID);
    public static final DeferredHolder<MenuType<?>, MenuType<ARCMenu>> ARC = MENUS.register("arc_menu", () -> new MenuType<>(ARCMenu::new, FeatureFlags.DEFAULT_FLAGS));

    public static void register(IEventBus modbus) {
        MENUS.register(modbus);
    }
}
