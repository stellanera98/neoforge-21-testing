package stellanera.datagen_test.providers.loot;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponents;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.CopyComponentsFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import stellanera.test.common.block.ModBlocks;

import java.util.List;
import java.util.Set;

public class ModBlockLootSubProvider extends BlockLootSubProvider {
    public ModBlockLootSubProvider(HolderLookup.Provider lookupProvider) {
        super(Set.of(), FeatureFlags.DEFAULT_FLAGS, lookupProvider);
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return List.of((Block) ModBlocks.SOLID.get());
        //return ModBlocks.BLOCKS.getEntries().stream().map(e -> (Block) e.value()).toList();
    }

    @Override
    protected void generate() {
        add(ModBlocks.SOLID.get(), LootTable.lootTable()
                .withPool(
                        this.applyExplosionCondition(
                                ModBlocks.SOLID.get(),
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(ModBlocks.TANK_ITEM.get())
                                                        .apply(
                                                                CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                                        )
                                        )
                        )
                )
                );
    }

    // TODO copied from vanilla, potentially make fit for any DCs or remove
    protected LootTable.Builder createShulkerBoxDrop(Block block) {
        return LootTable.lootTable()
                .withPool(
                        this.applyExplosionCondition(
                                block,
                                LootPool.lootPool()
                                        .setRolls(ConstantValue.exactly(1.0F))
                                        .add(
                                                LootItem.lootTableItem(block)
                                                        .apply(
                                                                CopyComponentsFunction.copyComponents(CopyComponentsFunction.Source.BLOCK_ENTITY)
                                                                        .include(DataComponents.CUSTOM_NAME)
                                                                        .include(DataComponents.CONTAINER)
                                                                        .include(DataComponents.LOCK)
                                                                        .include(DataComponents.CONTAINER_LOOT)
                                                        )
                                        )
                        )
                );
    }

}
