package stellanera.test.common.tile;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import stellanera.test.Modname;
import stellanera.test.common.block.ModBlocks;

public class ModTiles {
    public static final DeferredRegister<BlockEntityType<?>> TILES = DeferredRegister.create(BuiltInRegistries.BLOCK_ENTITY_TYPE, Modname.MODID);

    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<SolidTile>> SOLID_TYPE = TILES.register("solid_type", () -> BlockEntityType.Builder.of(SolidTile::new, ModBlocks.SOLID.get()).build(null));
    public static final DeferredHolder<BlockEntityType<?>, BlockEntityType<ARCTile>> ARC_TYPE = TILES.register("arc_type", () -> BlockEntityType.Builder.of(ARCTile::new, ModBlocks.ARC.get()).build(null));

    public static void register(IEventBus modbus) {
        TILES.register(modbus);
        modbus.addListener(ModTiles::registerCaps);
    }

    private static void registerCaps(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, SOLID_TYPE.get(), (tile, side) -> SolidTile.getHandler(tile));
    }
}
