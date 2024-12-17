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
    public long insert(long maxAmount, TransactionContext transaction) {
        return 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return maxAmount;
    }

    @Override
    public long getAmount() {
        return Long.MAX_VALUE;
    }

    @Override
    public long getCapacity() {
        return Long.MAX_VALUE;
    }
}
