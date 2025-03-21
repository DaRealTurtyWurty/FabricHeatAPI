package dev.turtywurty.heatapi.api.base;

import dev.turtywurty.heatapi.api.HeatStorage;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;

import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class PredicateHeatStorage implements HeatStorage {
    protected final Supplier<HeatStorage> delegate;
    protected final BooleanSupplier validInsertion, validExtraction;

    public PredicateHeatStorage(Supplier<HeatStorage> delegate, BooleanSupplier validInsertion, BooleanSupplier validExtraction) {
        this.delegate = delegate;
        this.validInsertion = validInsertion;
        this.validExtraction = validExtraction;
    }

    public PredicateHeatStorage(HeatStorage delegate, BooleanSupplier valid) {
        this(() -> delegate, valid, valid);
    }

    public PredicateHeatStorage(HeatStorage delegate, BooleanSupplier validInsertion, BooleanSupplier validExtraction) {
        this(() -> delegate, validInsertion, validExtraction);
    }

    public PredicateHeatStorage(Supplier<HeatStorage> delegate, BooleanSupplier valid) {
        this(delegate, valid, valid);
    }

    @Override
    public boolean supportsInsertion() {
        return this.validInsertion.getAsBoolean() && this.delegate.get().supportsInsertion();
    }

    @Override
    public boolean supportsExtraction() {
        return this.validExtraction.getAsBoolean() && this.delegate.get().supportsExtraction();
    }

    @Override
    public double insert(double maxAmount, TransactionContext transaction) {
        return this.validInsertion.getAsBoolean() ? this.delegate.get().insert(maxAmount, transaction) : 0;
    }

    @Override
    public double extract(double maxAmount, TransactionContext transaction) {
        return this.validExtraction.getAsBoolean() ? this.delegate.get().extract(maxAmount, transaction) : 0;
    }

    @Override
    public double getAmount() {
        return this.delegate.get().getAmount();
    }

    @Override
    public double getCapacity() {
        return this.delegate.get().getCapacity();
    }
}
