package dev.turtywurty.heatapi.api.unit;

public class RømerUnit implements HeatUnit {
    @Override
    public String getUnitName() {
        return "Rømer";
    }

    @Override
    public String getUnitSymbol() {
        return "°Rø";
    }

    @Override
    public double convertToBaseUnit(double value) {
        return (value - 7.5) * 40 / 21;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return value * 21 / 40 + 7.5;
    }
}
