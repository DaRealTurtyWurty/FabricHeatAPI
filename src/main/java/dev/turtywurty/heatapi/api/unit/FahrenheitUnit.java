package dev.turtywurty.heatapi.api.unit;

public class FahrenheitUnit implements HeatUnit {
    @Override
    public String getUnitName() {
        return "Fahrenheit";
    }

    @Override
    public String getUnitSymbol() {
        return "°F";
    }

    @Override
    public double convertToBaseUnit(double value) {
        return (value - 32) * 5 / 9;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return value * 9 / 5 + 32;
    }
}
