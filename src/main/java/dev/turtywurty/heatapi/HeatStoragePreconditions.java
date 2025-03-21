package dev.turtywurty.heatapi;

public class HeatStoragePreconditions {
    public static void notNegative(double value) {
        if (value < 0) {
            throw new IllegalArgumentException("Value cannot be negative!");
        }
    }
}
