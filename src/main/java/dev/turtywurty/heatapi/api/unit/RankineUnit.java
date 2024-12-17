package dev.turtywurty.heatapi.api.unit;

public class RankineUnit implements HeatUnit {
    @Override
    public String getUnitName() {
        return "Rankine";
    }

    @Override
    public String getUnitSymbol() {
        return "°R";
    }

    @Override
    public double convertToBaseUnit(double value) {
        return (value - 491.67) * 5.0 / 9.0;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return value * 9.0 / 5.0 + 491.67;
    }
}
