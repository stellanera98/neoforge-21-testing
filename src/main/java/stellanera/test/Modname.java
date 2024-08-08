package stellanera.test;

import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.javafmlmod.FMLModContainer;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import stellanera.test.common.block.ModBlocks;
import stellanera.test.common.datacomponent.ModComponents;
import stellanera.test.common.item.ModItems;
import stellanera.test.common.fluid.ModFluids;
import stellanera.test.common.menu.ModMenus;
import stellanera.test.common.recipe.ModRecipes;
import stellanera.test.common.tile.SolidTile;
import stellanera.test.common.tile.ModTiles;


@Mod(Modname.MODID)
public class Modname {
    public static final String MODID = "test";
    public static final Logger LOGGER = LogUtils.getLogger();

    public Modname(IEventBus modbus, ModContainer container, FMLModContainer fmlContainer, Dist dist) {
        ModFluids.register(modbus);
        ModBlocks.register(modbus, NeoForge.EVENT_BUS);
        ModTiles.register(modbus);
        ModItems.register(modbus);
        ModComponents.register(modbus);
        ModRecipes.register(modbus);
        ModMenus.register(modbus);
    }

    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MODID, path);
    }
}
