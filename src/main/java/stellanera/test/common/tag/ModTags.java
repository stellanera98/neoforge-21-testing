package stellanera.test.common.tag;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import stellanera.test.Modname;

public class ModTags {
    public static class Items {
        public static final TagKey<Item> ARC_TOOL = TagKey.create(Registries.ITEM, Modname.rl("arc/tool"));
        public static final TagKey<Item> ARC_TOOL_FURNACE = TagKey.create(Registries.ITEM, Modname.rl("arc/furnace"));
        public static final TagKey<Item> ARC_TOOL_BLASTING = TagKey.create(Registries.ITEM, Modname.rl("arc/blasting"));
        public static final TagKey<Item> ARC_TOOL_SMOKING = TagKey.create(Registries.ITEM, Modname.rl("arc/smoking"));
        public static final TagKey<Item> ARC_TOOL_MYTHING = TagKey.create(Registries.ITEM, Modname.rl("arc/mything"));
    }

    public static class Blocks {
    }

    private static ResourceLocation common(String path) {
        return ResourceLocation.fromNamespaceAndPath("c", path);
    }
}
