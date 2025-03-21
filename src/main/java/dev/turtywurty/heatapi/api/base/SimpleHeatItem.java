package dev.turtywurty.heatapi.api.base;

import dev.turtywurty.heatapi.api.HeatStorage;
import dev.turtywurty.heatapi.impl.SimpleItemHeatStorageImpl;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public interface SimpleHeatItem {
    static HeatStorage createStorage(ContainerItemContext context, double capacity, double maxInsert, double maxExtract) {
        return SimpleItemHeatStorageImpl.createSimpleStorage(context, capacity, maxInsert, maxExtract);
    }

    static double getHeatStoredUnchecked(ItemStack stack) {
        return stack.getOrDefault(HeatStorage.HEAT_COMPONENT, 0D);
    }

    static double getHeatStoredUnchecked(ItemVariant variant) {
        return getHeatStoredUnchecked(variant.getComponents());
    }

    static double getHeatStoredUnchecked(ComponentChanges changes) {
        Optional<Optional<Double>> value = Optional.ofNullable(changes.get(HeatStorage.HEAT_COMPONENT));
        return value.map(aDouble -> aDouble.orElse(0D)).orElse(0D);
    }

    static void setHeatStoredUnchecked(ItemStack stack, double amount) {
        if (amount <= 0) {
            stack.remove(HeatStorage.HEAT_COMPONENT);
        } else {
            stack.set(HeatStorage.HEAT_COMPONENT, amount);
        }
    }

    double getHeatCapacity(ItemStack stack);

    double getHeatMaxInput(ItemStack stack);

    double getHeatMaxOutput(ItemStack stack);

    default double getHeatStored(ItemStack stack) {
        return getHeatStoredUnchecked(stack);
    }

    default void setHeatStored(ItemStack stack, double amount) {
        setHeatStoredUnchecked(stack, amount);
    }
}
