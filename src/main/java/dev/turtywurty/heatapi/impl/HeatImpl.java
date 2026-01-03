package dev.turtywurty.heatapi.impl;

import com.mojang.serialization.Codec;
import dev.turtywurty.heatapi.api.HeatStorage;
import dev.turtywurty.heatapi.api.base.SimpleHeatItem;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.Registry;
import net.minecraft.resources.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HeatImpl {
    public static final String MOD_ID = "heatapi";
    public static final DataComponentType<Double> HEAT_COMPONENT = DataComponentType.<Double>builder()
            .persistent(Codec.DOUBLE)
            .networkSynchronized(ByteBufCodecs.DOUBLE)
            .build();

    public static Identifier id(String path) {
        return Identifier.fromNamespaceAndPath(MOD_ID, path);
    }

    public static void init() {
        Registry.register(BuiltInRegistries.DATA_COMPONENT_TYPE, id("heat"), HEAT_COMPONENT);
        HeatStorage.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof SimpleHeatItem item) {
                return SimpleHeatItem.createStorage(context, item.getHeatCapacity(stack), item.getHeatMaxInput(stack), item.getHeatMaxOutput(stack));
            }

            return null;
        });
    }
}
