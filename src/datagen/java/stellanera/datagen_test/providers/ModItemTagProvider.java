package stellanera.datagen_test.providers;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;
import stellanera.test.Modname;
import stellanera.test.common.item.ModItems;
import stellanera.test.common.tag.ModTags;

import java.util.concurrent.CompletableFuture;

public class ModItemTagProvider extends ItemTagsProvider {
    public ModItemTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, Modname.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(ModTags.Items.ARC_TOOL).addTags(ModTags.Items.ARC_TOOL_BLASTING, ModTags.Items.ARC_TOOL_FURNACE, ModTags.Items.ARC_TOOL_SMOKING, ModTags.Items.ARC_TOOL_MYTHING);
        tag(ModTags.Items.ARC_TOOL_FURNACE).add(ModItems.ARC_TOOL_FURNACE.get(), ModItems.PRIMITIVE_ARC_TOOL_FURNACE.get(), ModItems.HELLFORGED_ARC_TOOL_FURNACE.get());
        tag(ModTags.Items.ARC_TOOL_BLASTING).add(ModItems.ARC_TOOL_BLASTING.get(), ModItems.PRIMITIVE_ARC_TOOL_BLASTING.get(), ModItems.HELLFORGED_ARC_TOOL_BLASTING.get());
        tag(ModTags.Items.ARC_TOOL_SMOKING).add(ModItems.ARC_TOOL_SMOKING.get(), ModItems.PRIMITIVE_ARC_TOOL_SMOKING.get(), ModItems.HELLFORGED_ARC_TOOL_SMOKING.get());
        tag(ModTags.Items.ARC_TOOL_MYTHING).add(ModItems.ARC_TOOL_MYTHING.get(), ModItems.PRIMITIVE_ARC_TOOL_MYTHING.get(), ModItems.HELLFORGED_ARC_TOOL_MYTHING.get());
    }
}
