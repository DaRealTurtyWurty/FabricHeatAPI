package dev.turtywurty.heatapi.api.unit;

public class KelvinUnit implements HeatUnit {
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
