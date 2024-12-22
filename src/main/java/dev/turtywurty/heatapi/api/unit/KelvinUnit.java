package dev.turtywurty.heatapi.api.unit;

public class KelvinUnit implements HeatUnit {
    public static final double WATER_FREEZES = 273.15;
    public static final double WATER_BOILS = 373.15;
    public static final double ABSOLUTE_ZERO = 0.0;
    public static final double ROOM_TEMPERATURE = 294.15;
    public static final double LAVA_TEMPERATURE = 1273.15;

    @Override
    public String getUnitName() {
        return "Kelvin";
    }

    @Override
    public String getUnitSymbol() {
        return "K";
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value - 273.15;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return value + 273.15;
    }
}
