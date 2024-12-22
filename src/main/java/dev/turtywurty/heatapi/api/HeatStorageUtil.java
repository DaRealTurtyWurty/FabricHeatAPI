package dev.turtywurty.heatapi.api;

import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public final class HeatStorageUtil {
    private HeatStorageUtil() {
        throw new UnsupportedOperationException("HeatStorageUtil is a utility class and should not be instantiated!");
    }

    /**
     * Move heat between two heat storages, and return the amount that was successfully moved.
     *
     * @param from        The source storage. May be null.
     * @param to          The target storage. May be null.
     * @param maxAmount   The maximum amount that may be moved.
     * @param transaction The transaction this transfer is part of,
     *                    or {@code null} if a transaction should be opened just for this transfer.
     * @return The amount of heat that was successfully moved.
     */
    public static long move(@Nullable HeatStorage from, @Nullable HeatStorage to, long maxAmount, @Nullable TransactionContext transaction) {
        if (from == null || to == null)
            return 0;

        StoragePreconditions.notNegative(maxAmount);

        long maxExtracted;
        try (Transaction extractionTestTransaction = Transaction.openNested(transaction)) {
            maxExtracted = from.extract(maxAmount, extractionTestTransaction);
        }

        try (Transaction moveTransaction = Transaction.openNested(transaction)) {
            long accepted = to.insert(maxExtracted, moveTransaction);

            if (from.extract(accepted, moveTransaction) == accepted) {
                moveTransaction.commit();
                return accepted;
            }
        }

        return 0;
    }

    /**
     * Return true if the passed stack offers a heat storage through {@link HeatStorage#ITEM}.
     * This can typically be used for inventories or slots that want to accept heat storages only.
     */
    public static boolean isEnergyStorage(ItemStack stack) {
        return ContainerItemContext.withConstant(stack).find(HeatStorage.ITEM) != null;
    }
}
