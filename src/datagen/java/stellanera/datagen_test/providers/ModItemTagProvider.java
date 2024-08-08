package stellanera.datagen_test.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import stellanera.test.Modname;
import stellanera.test.common.item.ModItems;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Modname.MODID, existingFileHelper);
    }

    public static final TagKey<Item> ARC_TOOL = TagKey.create(Registries.ITEM, Modname.rl("arc/tool"));
    public static final TagKey<Item> ARC_TOOL_FURNACE = TagKey.create(Registries.ITEM, Modname.rl("arc/furnace"));
    public static final TagKey<Item> ARC_TOOL_BLASTING = TagKey.create(Registries.ITEM, Modname.rl("arc/blasting"));
    public static final TagKey<Item> ARC_TOOL_MYTHING = TagKey.create(Registries.ITEM, Modname.rl("arc/mything"));

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ARC_TOOL).addTags(ARC_TOOL_BLASTING, ARC_TOOL_FURNACE, ARC_TOOL_MYTHING);
        tag(ARC_TOOL_FURNACE).add(ModItems.ARC_TOOL_FURNACE.get(), ModItems.PRIMITIVE_ARC_TOOL_FURNACE.get(), ModItems.HELLFORGED_ARC_TOOL_FURNACE.get());
        tag(ARC_TOOL_BLASTING).add(ModItems.ARC_TOOL_BLASTING.get(), ModItems.PRIMITIVE_ARC_TOOL_BLASTING.get(), ModItems.HELLFORGED_ARC_TOOL_BLASTING.get());
        tag(ARC_TOOL_MYTHING).add(ModItems.ARC_TOOL_MYTHING.get(), ModItems.PRIMITIVE_ARC_TOOL_MYTHING.get(), ModItems.HELLFORGED_ARC_TOOL_MYTHING.get());
    }
}
