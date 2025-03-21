package dev.turtywurty.heatapi.impl;

import com.mojang.serialization.Codec;
import dev.turtywurty.heatapi.api.HeatStorage;
import dev.turtywurty.heatapi.api.base.SimpleHeatItem;
import net.minecraft.component.ComponentType;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class HeatImpl {
    public static final String MOD_ID = "heatapi";
    public static final ComponentType<Double> HEAT_COMPONENT = ComponentType.<Double>builder()
            .codec(Codec.DOUBLE)
            .packetCodec(PacketCodecs.DOUBLE)
            .build();

    public static Identifier id(String path) {
        return Identifier.of(MOD_ID, path);
    }

    public static void init() {
        Registry.register(Registries.DATA_COMPONENT_TYPE, id("heat"), HEAT_COMPONENT);
        HeatStorage.ITEM.registerFallback((stack, context) -> {
            if (stack.getItem() instanceof SimpleHeatItem item) {
                return SimpleHeatItem.createStorage(context, item.getHeatCapacity(stack), item.getHeatMaxInput(stack), item.getHeatMaxOutput(stack));
            }

            return null;
        });
    }
}
