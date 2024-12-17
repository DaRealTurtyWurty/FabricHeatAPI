package dev.turtywurty.heatapi.api.base;

import dev.turtywurty.heatapi.api.HeatStorage;
import dev.turtywurty.heatapi.impl.SimpleItemHeatStorageImpl;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.minecraft.component.ComponentChanges;
import net.minecraft.item.ItemStack;

import java.util.Optional;

public interface SimpleHeatItem {
    static HeatStorage createStorage(ContainerItemContext context, long capacity, long maxInsert, long maxExtract) {
        return SimpleItemHeatStorageImpl.createSimpleStorage(context, capacity, maxInsert, maxExtract);
    }

    long getHeatCapacity(ItemStack stack);

    long getHeatMaxInput(ItemStack stack);

    long getHeatMaxOutput(ItemStack stack);

    default long getHeatStored(ItemStack stack) {
        return getHeatStoredUnchecked(stack);
    }

    default void setHeatStored(ItemStack stack, long amount) {
        setHeatStoredUnchecked(stack, amount);
    }

    static long getHeatStoredUnchecked(ItemStack stack) {
        return stack.getOrDefault(HeatStorage.HEAT_COMPONENT, 0L);
    }

    static long getHeatStoredUnchecked(ItemVariant variant) {
        return getHeatStoredUnchecked(variant.getComponents());
    }

    static long getHeatStoredUnchecked(ComponentChanges changes) {
        Optional<Optional<Long>> value = Optional.ofNullable(changes.get(HeatStorage.HEAT_COMPONENT));
        return value.map(aLong -> aLong.orElse(0L)).orElse(0L);
    }

    static void setHeatStoredUnchecked(ItemStack stack, long amount) {
        if(amount <= 0) {
            stack.remove(HeatStorage.HEAT_COMPONENT);
        } else {
            stack.set(HeatStorage.HEAT_COMPONENT, amount);
        }
    }
}
