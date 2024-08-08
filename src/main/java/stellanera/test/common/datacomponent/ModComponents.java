package stellanera.test.common.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import stellanera.test.Modname;

public class ModComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Modname.MODID);

    public static final Codec<RecordFluidStack> FLUID_STACK_CODEC = RecordCodecBuilder.create(instance -> instance.group(FluidStack.CODEC.fieldOf("fluid_content").forGetter(RecordFluidStack::stack)).apply(instance, RecordFluidStack::new));

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<RecordFluidStack>> FLUID_CONTENT = COMPONENTS.registerComponentType("fluid_content", builder -> builder.persistent(FLUID_STACK_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TANK_TIER = COMPONENTS.registerComponentType("tank_tier", builder -> builder.persistent(Codec.INT));

    public static void register(IEventBus modbus) {
        COMPONENTS.register(modbus);
    }
}

