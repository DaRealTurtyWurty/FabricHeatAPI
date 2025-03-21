package dev.turtywurty.heatapi.impl;

import dev.turtywurty.heatapi.HeatStoragePreconditions;
import dev.turtywurty.heatapi.api.HeatStorage;
import dev.turtywurty.heatapi.api.base.PredicateHeatStorage;
import dev.turtywurty.heatapi.api.base.SimpleHeatItem;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.item.ItemVariant;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

@ApiStatus.Internal
public class SimpleItemHeatStorageImpl implements HeatStorage {
    private final ContainerItemContext context;
    private final double capacity;
    private final double maxInsert, maxExtract;

    private SimpleItemHeatStorageImpl(@NotNull ContainerItemContext context, double capacity, double maxInsert, double maxExtract) {
        this.context = context;
        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    public static HeatStorage createSimpleStorage(ContainerItemContext context, double capacity, double maxInsert, double maxExtract) {
        Objects.requireNonNull(context);
        HeatStoragePreconditions.notNegative(capacity);
        HeatStoragePreconditions.notNegative(maxInsert);
        HeatStoragePreconditions.notNegative(maxExtract);

        Item startingItem = context.getItemVariant().getItem();
        return new PredicateHeatStorage(new SimpleItemHeatStorageImpl(context, capacity, maxInsert, maxExtract),
                () -> context.getItemVariant().isOf(startingItem) && context.getAmount() > 0);
    }

    private boolean trySetHeat(double heatAmountPerCount, long count, TransactionContext transaction) {
        ItemStack newStack = context.getItemVariant().toStack();
        SimpleHeatItem.setHeatStoredUnchecked(newStack, heatAmountPerCount);
        var newVariant = ItemVariant.of(newStack);

        try (Transaction nested = transaction.openNested()) {
            if (context.extract(context.getItemVariant(), count, nested) == count && context.insert(newVariant, count, nested) == count) {
                nested.commit();
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean supportsInsertion() {
        return maxInsert > 0;
    }

    @Override
    public boolean supportsExtraction() {
        return maxExtract > 0;
    }

    @Override
    public double insert(double maxAmount, TransactionContext transaction) {
        long count = context.getAmount();
        double maxAmountPerCount = maxAmount / count;
        double currentAmountPerCount = getAmount() / count;
        double insertedPerCount = Math.min(maxInsert, Math.min(maxAmountPerCount, capacity - currentAmountPerCount));

        if (insertedPerCount <= 0)
            return 0;

        return trySetHeat(currentAmountPerCount + insertedPerCount, count, transaction) ? insertedPerCount * count : 0;
    }

    @Override
    public double extract(double maxAmount, TransactionContext transaction) {
        long count = context.getAmount();
        double maxAmountPerCount = maxAmount / count;
        double currentAmountPerCount = getAmount() / count;
        double extractedPerCount = Math.min(maxExtract, Math.min(maxAmountPerCount, currentAmountPerCount));

        if (extractedPerCount <= 0)
            return 0;

        return trySetHeat(currentAmountPerCount - extractedPerCount, count, transaction) ? extractedPerCount * count : 0;
    }

    @Override
    public double getAmount() {
        return context.getAmount() * SimpleHeatItem.getHeatStoredUnchecked(context.getItemVariant().getComponents());
    }

    @Override
    public double getCapacity() {
        return capacity * context.getAmount();
    }
}
