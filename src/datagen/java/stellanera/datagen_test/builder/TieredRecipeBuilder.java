package stellanera.datagen_test.builder;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.advancements.Criterion;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.recipes.RecipeBuilder;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapedRecipePattern;
import net.minecraft.world.level.ItemLike;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import org.jetbrains.annotations.Nullable;
import stellanera.test.common.recipe.TieredRecipe;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class TieredRecipeBuilder implements RecipeBuilder {
    private final RecipeCategory category;
    private final int count;
    private final ItemStack resultStack; // Neo: add stack result support
    private final Item result;
    private final List<String> rows = Lists.newArrayList();
    private final Map<Character, Ingredient> key = Maps.newLinkedHashMap();
    private final Map<String, Criterion<?>> criteria = new LinkedHashMap<>();
    private final int primary;
    private final int secondary;
    @Nullable
    private String group;
    private boolean showNotification = true;

    public TieredRecipeBuilder(RecipeCategory category, ItemStack tieredStack, int primary, int secondary) {
        this.category = category;
        this.resultStack = tieredStack;
        this.result = tieredStack.getItem();
        this.count = tieredStack.getCount();
        this.primary = primary;
        this.secondary = secondary;
    }

    public TieredRecipeBuilder(RecipeCategory category, Item result, int primary, int secondary) {
        this(category, new ItemStack(result, 1), primary, secondary);
    }

    public static TieredRecipeBuilder shaped(RecipeCategory category, Item result, int primary, int secondary) {
        return new TieredRecipeBuilder(category, result, primary, secondary);
    }

    public TieredRecipeBuilder pattern(String pattern) {
        if (!this.rows.isEmpty() && pattern.length() != this.rows.get(0).length()) {
            throw new IllegalArgumentException("Pattern must be the same with on every line!");
        } else {
            this.rows.add(pattern);
            return this;
        }
    }

    public TieredRecipeBuilder define(Character symbol, Ingredient ingredient) {
        if (this.key.containsKey(symbol)) {
            throw new IllegalArgumentException("Symbol '" + symbol + "' is already defined!");
        } else if (symbol == ' ') {
            throw new IllegalArgumentException("Symbol ' ' (whitespace) is reserved and cannot be defined!");
        } else {
            this.key.put(symbol, ingredient);
            return this;
        }
    }

    public TieredRecipeBuilder define(Character symbol, TagKey<Item> tag) {
        return this.define(symbol, Ingredient.of(tag));
    }

    public TieredRecipeBuilder define(Character symbol, ItemLike item) {
        return this.define(symbol, Ingredient.of(item));
    }

    @Override
    public RecipeBuilder unlockedBy(String name, Criterion<?> criterion) {
        return this;
    }

    @Override
    public RecipeBuilder group(@Nullable String groupName) {
        return this;
    }

    @Override
    public Item getResult() {
        return this.resultStack.getItem();
    }

    @Override
    public void save(RecipeOutput recipeOutput, ResourceLocation id) {
        ShapedRecipePattern shapedRecipePattern = ShapedRecipePattern.of(this.key, this.rows);
        int max = shapedRecipePattern.height() * shapedRecipePattern.width();
        TieredRecipe tieredRecipe = new TieredRecipe(
                shapedRecipePattern,
                this.resultStack,
                this.primary,
                this.secondary
        );
        recipeOutput.accept(id, tieredRecipe, null);
    }
}
