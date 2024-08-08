package stellanera.test.common.recipe;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.SimpleCraftingRecipeSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import stellanera.test.Modname;


public class ModRecipes {
    public static DeferredRegister<RecipeSerializer<?>> SERIALIZERS = DeferredRegister.create(Registries.RECIPE_SERIALIZER, Modname.MODID);

    public static DeferredHolder<RecipeSerializer<?>, RecipeSerializer<TankRecipe>> TANK_SERIALIZER = SERIALIZERS.register("crafting_special_tank", () -> new SimpleCraftingRecipeSerializer<>(TankRecipe::new));

    public static void register(IEventBus modbus) {
        SERIALIZERS.register(modbus);
    }
}
