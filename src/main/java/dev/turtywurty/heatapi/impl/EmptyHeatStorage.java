package dev.turtywurty.heatapi.impl;

import dev.turtywurty.heatapi.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class EmptyHeatStorage implements HeatStorage {
    public static final EmptyHeatStorage INSTANCE = new EmptyHeatStorage();

    private EmptyHeatStorage() {
    }

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public boolean supportsExtraction() {
        return false;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long getAmount() {
        return 0;
    }

    @Override
    public long getCapacity() {
        return 0;
    }
}
