package dev.turtywurty.heatapi.api.base;

import dev.turtywurty.heatapi.HeatStoragePreconditions;
import dev.turtywurty.heatapi.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public class SimpleHeatStorage extends SnapshotParticipant<Double> implements HeatStorage {
    public final double capacity;
    public final double maxInsert, maxExtract;
    private double amount = 0;

    public SimpleHeatStorage(double capacity, double maxInsert, double maxExtract) {
        HeatStoragePreconditions.notNegative(capacity);
        HeatStoragePreconditions.notNegative(maxInsert);
        HeatStoragePreconditions.notNegative(maxExtract);

        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    @Override
    public double insert(double maxAmount, TransactionContext transaction) {
        double inserted = Math.min(maxAmount, Math.min(this.maxInsert, this.capacity - getAmount()));
        if (inserted > 0) {
            updateSnapshots(transaction);
            setAmount(getAmount() + inserted);
            return inserted;
        }

        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return this.maxExtract > 0;
    }

    @Override
    public double extract(double maxAmount, TransactionContext transaction) {
        double extracted = Math.min(maxAmount, Math.min(this.maxExtract, getAmount()));
        if (extracted > 0) {
            updateSnapshots(transaction);
            setAmount(getAmount() - extracted);
            return extracted;
        }

        return 0;
    }

    @Override
    public boolean supportsInsertion() {
        return this.maxInsert > 0;
    }

    @Override
    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    @Override
    public double getCapacity() {
        return this.capacity;
    }

    @Override
    protected Double createSnapshot() {
        return getAmount();
    }

    @Override
    protected void readSnapshot(Double snapshot) {
        setAmount(snapshot);
    }
}
