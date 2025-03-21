package dev.turtywurty.heatapi.api.base;

import dev.turtywurty.heatapi.HeatStoragePreconditions;
import dev.turtywurty.heatapi.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

public class LimitingHeatStorage implements HeatStorage {
    protected final HeatStorage delegate;
    protected final double maxInsert, maxExtract;

    public LimitingHeatStorage(@NotNull HeatStorage delegate, double maxInsert, double maxExtract) {
        Objects.requireNonNull(delegate);
        HeatStoragePreconditions.notNegative(maxInsert);
        HeatStoragePreconditions.notNegative(maxExtract);

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
    public double insert(double maxAmount, TransactionContext transaction) {
        return this.delegate.insert(Math.min(maxAmount, this.maxInsert), transaction);
    }

    @Override
    public double extract(double maxAmount, TransactionContext transaction) {
        return this.delegate.extract(Math.min(maxAmount, this.maxExtract), transaction);
    }

    @Override
    public double getAmount() {
        return this.delegate.getAmount();
    }

    @Override
    public double getCapacity() {
        return this.delegate.getCapacity();
    }
}
