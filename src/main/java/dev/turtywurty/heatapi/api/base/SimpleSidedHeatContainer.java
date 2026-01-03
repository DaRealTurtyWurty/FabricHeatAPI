package dev.turtywurty.heatapi.api.base;

import dev.turtywurty.heatapi.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.base.SnapshotParticipant;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * A base energy storage implementation with a dynamic capacity, and per-side per-operation insertion and extraction limits.
 * {@link #getSideStorage} can be used to get an {@code HeatStorage} implementation for a given side.
 * Make sure to override {@link #onFinalCommit} to call {@code markDirty} and similar functions.
 * <p>
 * This class is a {@link SnapshotParticipant} and will automatically handle snapshots for you.
 */
public abstract class SimpleSidedHeatContainer extends SnapshotParticipant<Double> {
    private final SideStorage[] sideStorages = new SideStorage[Direction.values().length + 1];
    private double amount;

    public SimpleSidedHeatContainer() {
        for (int i = 0; i < this.sideStorages.length; i++) {
            this.sideStorages[i] = (i == Direction.values().length - 1) ? null : new SideStorage(Direction.from3DDataValue(i));
        }
    }

    /**
     * @return the capacity of this storage
     */
    public abstract double getCapacity();

    /**
     * Get the maximum amount of heat that can be inserted into this storage in a single operation.
     *
     * @param side the side to insert from, or null for internal insertion
     *             (e.g. from a machine's internal buffer)
     * @return the maximum amount of heat that can be inserted
     */
    public abstract double getMaxInsert(@Nullable Direction side);

    /**
     * Get the maximum amount of heat that can be extracted from this storage in a single operation.
     *
     * @param side the side to extract to, or null for internal extraction
     *             (e.g. to a machine's internal buffer)
     * @return the maximum amount of heat that can be extracted
     */
    public abstract double getMaxExtract(@Nullable Direction side);

    /**
     * Get the capacity of the storage on a given side.
     *
     * @param side the side to get the capacity for, or null for the internal capacity
     * @return the capacity of the storage on the given side
     */
    public HeatStorage getSideStorage(@Nullable Direction side) {
        return this.sideStorages[side == null ? Direction.values().length - 1 : side.get3DDataValue()];
    }

    @Override
    protected Double createSnapshot() {
        return this.amount;
    }

    @Override
    protected void readSnapshot(Double snapshot) {
        this.amount = snapshot;
    }

    public double getAmount() {
        return this.amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    private class SideStorage implements HeatStorage {
        private final Direction side;

        public SideStorage(Direction side) {
            this.side = side;
        }

        public Direction getSide() {
            return this.side;
        }

        @Override
        public boolean supportsInsertion() {
            return getMaxInsert(this.side) > 0;
        }

        @Override
        public boolean supportsExtraction() {
            return getMaxExtract(this.side) > 0;
        }

        @Override
        public double insert(double maxAmount, TransactionContext transaction) {
            double inserted = Math.min(getMaxInsert(side), Math.min(maxAmount, getCapacity() - amount));

            if (inserted > 0) {
                updateSnapshots(transaction);
                amount += inserted;
                return inserted;
            }

            return 0;
        }

        @Override
        public double extract(double maxAmount, TransactionContext transaction) {
            double extracted = Math.min(getMaxExtract(side), Math.min(maxAmount, amount));

            if (extracted > 0) {
                updateSnapshots(transaction);
                amount -= extracted;
                return extracted;
            }

            return 0;
        }

        @Override
        public double getAmount() {
            return amount;
        }

        @Override
        public double getCapacity() {
            return SimpleSidedHeatContainer.this.getCapacity();
        }
    }
}
