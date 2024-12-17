package dev.turtywurty.heatapi.api.base;

import dev.turtywurty.heatapi.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LimitingHeatStorage implements HeatStorage {
    protected final HeatStorage delegate;
    protected final long maxInsert, maxExtract;

    public LimitingHeatStorage(@NotNull HeatStorage delegate, long maxInsert, long maxExtract) {
        Objects.requireNonNull(delegate);
        StoragePreconditions.notNegative(maxInsert);
        StoragePreconditions.notNegative(maxExtract);

        this.delegate = delegate;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    @Override
    public boolean supportsInsertion() {
        return this.maxInsert > 0 && this.delegate.supportsInsertion();
    }

    @Override
    public boolean supportsExtraction() {
        return this.maxExtract > 0 && this.delegate.supportsExtraction();
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        return this.delegate.insert(Math.min(maxAmount, this.maxInsert), transaction);
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        return this.delegate.extract(Math.min(maxAmount, this.maxExtract), transaction);
    }

    @Override
    public long getAmount() {
        return this.delegate.getAmount();
    }

    @Override
    public long getCapacity() {
        return this.delegate.getCapacity();
    }
}
