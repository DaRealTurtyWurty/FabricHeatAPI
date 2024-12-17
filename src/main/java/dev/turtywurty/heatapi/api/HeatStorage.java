package dev.turtywurty.heatapi.api;

import dev.turtywurty.heatapi.api.base.PredicateHeatStorage;
import dev.turtywurty.heatapi.api.base.SimpleHeatItem;
import dev.turtywurty.heatapi.api.base.SimpleHeatStorage;
import dev.turtywurty.heatapi.api.base.SimpleSidedHeatContainer;
import dev.turtywurty.heatapi.impl.EmptyHeatStorage;
import dev.turtywurty.heatapi.impl.HeatImpl;
import dev.turtywurty.heatapi.impl.SimpleItemHeatStorageImpl;
import net.fabricmc.fabric.api.lookup.v1.block.BlockApiLookup;
import net.fabricmc.fabric.api.lookup.v1.item.ItemApiLookup;
import net.fabricmc.fabric.api.transfer.v1.context.ContainerItemContext;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.component.ComponentType;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A heat storage that can be queried for heat transfer.
 *
 * <p>Heat storages are used to store heat and transfer it between machines.
 * They can be queried for the amount of heat they contain, the maximum amount of heat they can store,
 * and the amount of heat they can accept or provide in a single operation.
 *
 * <p>
 *     <ul>
 *         <li>{@link #supportsInsertion()} and {@link #supportsExtraction()} can be used to determine if the storage can accept or provide heat.
 *         <li>{@link #insert} and {@link #extract} can be used to transfer heat to or from the storage.
 *         <li>{@link #getAmount()} and {@link #getCapacity()} can be used to query the current amount of heat and the maximum amount of heat that can be stored.
 *         <li>{@link #getRemainingCapacity()} can be used to query the remaining capacity of the storage.
 *         <li>{@link #getMinCapacity()} can be used to query the minimum amount of heat that can be stored in the storage.
 *         <li></li>
 *         <li>{@link #SIDED} can be used to query heat storages from blocks.
 *         <li>{@link #ITEM} can be used to query heat storages from items.
 *         <li>{@link #HEAT_COMPONENT} can be used to query heat storages from item stacks.
 *         <li>{@link #EMPTY} can be used as a default value for optional heat storages.
 *         <li></li>
 *         <li>{@link SimpleHeatStorage} and {@link SimpleSidedHeatContainer} are provided as base implementations.
 *     </ul>
 * </p>
 */
public interface HeatStorage {
    /**
     * Sided block access to heat storages.
     * The {@code Direction} parameter may be null, meaning that the full storage (ignoring side restrictions) should be queried.
     * Refer to {@link BlockApiLookup} for documentation on how to use this field.
     *
     * <p>The system is push based. That means that heat sources are responsible for pushing heat to nearby machines.
     * Machines and pipes should NOT pull heat from other sources.
     *
     * <p>{@link SimpleHeatStorage} and {@link SimpleSidedHeatContainer} are provided as base implementations.
     *
     * <p>When the operations supported by a heat storage change,
     * that is if the return value of {@link HeatStorage#supportsInsertion} or {@link HeatStorage#supportsExtraction} changes,
     * the storage should notify its neighbors with a block update so that they can refresh their connections if necessary.
     *
     * <p>This may be queried safely both on the logical server and on the logical client threads.
     * On the server thread (i.e. with a server world), all transfer functionality is always supported.
     * On the client thread (i.e. with a client world), contents of queried HeatStorages are unreliable and should not be modified.
     */
    BlockApiLookup<HeatStorage, @Nullable Direction> SIDED =
            BlockApiLookup.get(HeatImpl.id("sided_heat"), HeatStorage.class, Direction.class);

    /**
     * Item access to heat storages.
     * Querying should always happen through {@link ContainerItemContext#find}.
     *
     * <p>{@link SimpleItemHeatStorageImpl} is provided as an implementation example.
     * Instances of it can be obtained through {@link SimpleHeatItem#createStorage}.
     * Custom implementations should treat the context as a wrapper around a single slot,
     * and always check the current item variant and amount before any operation, like {@code SimpleItemHeatStorageImpl} does it.
     * The check can be handled by {@link PredicateHeatStorage}.
     *
     * <p>This may be queried both client-side and server-side.
     * Returned APIs should behave the same regardless of the logical side.
     */
    ItemApiLookup<HeatStorage, ContainerItemContext> ITEM =
            ItemApiLookup.get(HeatImpl.id("heat"), HeatStorage.class, ContainerItemContext.class);

    /**
     * An empty heat storage that does not support any operations.
     * This can be used as a default value for optional heat storages.
     */
    HeatStorage EMPTY = Objects.requireNonNull(EmptyHeatStorage.INSTANCE);

    /**
     * A component type for heat storages.
     *
     * <p><b>This component should only be used on item stacks from your mod.</b>
     * Otherwise, do not query it or assume it exists.
     * Inter-mod heat interactions should happen using {@link #ITEM}.</b>
     */
    ComponentType<Long> HEAT_COMPONENT = Objects.requireNonNull(HeatImpl.HEAT_COMPONENT);

    /**
     * Return false if calling {@link #insert} will absolutely always return 0, or true otherwise or in doubt.
     *
     * <p>Note: This function is meant to be used by pipes or other devices that can transfer heat to know if
     * they should interact with this storage at all.
     *
     * <p>It is not meant to be used by the storage itself to determine if it can insert heat into itself.
     */
    default boolean supportsInsertion() {
        return true;
    }

    /**
     * Try to insert up to {@code maxAmount} units of heat into this storage.
     *
     * @param maxAmount   the maximum amount of heat to insert (may not be negative)
     * @param transaction the transaction context that this operation is part of
     * @return (a non - negative) amount of heat that was actually inserted (or 0 if no heat was inserted)
     */
    long insert(long maxAmount, TransactionContext transaction);

    /**
     * Return false if calling {@link #extract} will absolutely always return 0, or true otherwise or in doubt.
     *
     * <p>Note: This function is meant to be used by pipes or other devices that can transfer heat to know if
     * they should interact with this storage at all.
     *
     * <p>It is not meant to be used by the storage itself to determine if it can extract heat from itself.
     */
    default boolean supportsExtraction() {
        return true;
    }

    /**
     * Try to extract up to {@code maxAmount} units of heat from this storage.
     *
     * @param maxAmount   the maximum amount of heat to extract (may not be negative)
     * @param transaction the transaction context that this operation is part of
     * @return (a non - negative) amount of heat that was actually extracted (or 0 if no heat was extracted)
     */
    long extract(long maxAmount, TransactionContext transaction);

    /**
     * Return the amount of heat currently stored in this storage.
     */
    long getAmount();

    /**
     * Return the maximum amount of heat that can be stored in this storage.
     */
    long getCapacity();

    /**
     * Return the remaining capacity of this storage.
     *
     * <p>This is equivalent to {@link #getCapacity()} - {@link #getAmount()}.
     */
    default long getRemainingCapacity() {
        return getCapacity() - getAmount();
    }

    /**
     * Return the minimum amount of heat that can be stored in this storage.
     *
     * <p>This defaults to 0, but can be overridden to provide a minimum amount of heat that must be stored in this storage.
     */
    default long getMinCapacity() {
        return 0;
    }
}
