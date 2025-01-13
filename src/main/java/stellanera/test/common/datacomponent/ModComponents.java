package stellanera.test.common.datacomponent;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.SimpleFluidContent;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;
import stellanera.test.Modname;

public class ModComponents {
    public static final DeferredRegister.DataComponents COMPONENTS = DeferredRegister.createDataComponents(Modname.MODID);

    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> TIER = COMPONENTS.registerComponentType("tier", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<SimpleFluidContent>> FLUID_CONTENT = COMPONENTS.registerComponentType("simple_fluid_content", builder -> builder.persistent(SimpleFluidContent.CODEC).networkSynchronized(SimpleFluidContent.STREAM_CODEC));
    public static final DeferredHolder<DataComponentType<?>, DataComponentType<Integer>> ENERGY_CONTENT = COMPONENTS.registerComponentType("simple_energy_content", builder -> builder.persistent(Codec.INT).networkSynchronized(ByteBufCodecs.INT));

    public static void register(IEventBus modbus) {
        COMPONENTS.register(modbus);
    }
}

