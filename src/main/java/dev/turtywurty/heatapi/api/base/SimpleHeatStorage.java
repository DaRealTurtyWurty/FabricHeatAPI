package dev.turtywurty.heatapi.api.base;

import dev.turtywurty.heatapi.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.storage.StoragePreconditions;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;

public class SimpleHeatStorage extends SnapshotParticipant<Long> implements HeatStorage {
    public final long capacity;
    public final long maxInsert, maxExtract;
    private long amount = 0;

    public SimpleHeatStorage(long capacity, long maxInsert, long maxExtract) {
        StoragePreconditions.notNegative(capacity);
        StoragePreconditions.notNegative(maxInsert);
        StoragePreconditions.notNegative(maxExtract);

        this.capacity = capacity;
        this.maxInsert = maxInsert;
        this.maxExtract = maxExtract;
    }

    @Override
    public long insert(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);

        long inserted = Math.min(maxAmount, Math.min(this.maxInsert, this.capacity - this.getAmount()));
        if (inserted > 0) {
            updateSnapshots(transaction);
            this.setAmount(this.getAmount() + inserted);
            return inserted;
        }

        return 0;
    }

    @Override
    public boolean supportsExtraction() {
        return this.maxExtract > 0;
    }

    @Override
    public long extract(long maxAmount, TransactionContext transaction) {
        StoragePreconditions.notNegative(maxAmount);

        long extracted = Math.min(maxAmount, Math.min(this.maxExtract, this.getAmount()));
        if (extracted > 0) {
            updateSnapshots(transaction);
            this.setAmount(this.getAmount() - extracted);
            return extracted;
        }

        return 0;
    }

    @Override
    public boolean supportsInsertion() {
        return this.maxInsert > 0;
    }

    @Override
    public long getAmount() {
        return this.amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    @Override
    public long getCapacity() {
        return this.capacity;
    }

    @Override
    protected Long createSnapshot() {
        return this.getAmount();
    }

    @Override
    protected void readSnapshot(Long snapshot) {
        this.setAmount(snapshot);
    }
}
