package dev.turtywurty.heatapi.api.unit;

public class CelsiusUnit implements HeatUnit {
    @Override
    public String getUnitName() {
        return "Celsius";
    }

    @Override
    public String getUnitSymbol() {
        return "°C";
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return value;
    }
}
