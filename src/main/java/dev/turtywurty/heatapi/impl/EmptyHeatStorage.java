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
    public double insert(double maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public double extract(double maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public double getAmount() {
        return 0;
    }

    @Override
    public double getCapacity() {
        return 0;
    }
}
