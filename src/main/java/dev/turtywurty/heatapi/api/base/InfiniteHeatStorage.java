package dev.turtywurty.heatapi.api.base;

import dev.turtywurty.heatapi.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

public class InfiniteHeatStorage implements HeatStorage {
    public static final InfiniteHeatStorage INSTANCE = new InfiniteHeatStorage();

    @Override
    public boolean supportsInsertion() {
        return false;
    }

    @Override
    public double insert(double maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public double extract(double maxAmount, TransactionContext transaction) {
        return maxAmount;
    }

    @Override
    public double getAmount() {
        return Double.MAX_VALUE;
    }

    @Override
    public double getCapacity() {
        return Double.MAX_VALUE;
    }
}
