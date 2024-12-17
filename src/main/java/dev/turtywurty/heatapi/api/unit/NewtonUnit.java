package dev.turtywurty.heatapi.api.unit;

public class NewtonUnit implements HeatUnit {
    @Override
    public String getUnitName() {
        return "Newton";
    }

    @Override
    public String getUnitSymbol() {
        return "°N";
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * 3.03030303;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return value / 3.03030303;
    }
}
