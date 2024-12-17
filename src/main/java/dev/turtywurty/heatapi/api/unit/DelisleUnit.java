package dev.turtywurty.heatapi.api.unit;

public class DelisleUnit implements HeatUnit {
    @Override
    public String getUnitName() {
        return "Delisle";
    }

    @Override
    public String getUnitSymbol() {
        return "°De";
    }

    @Override
    public double convertToBaseUnit(double value) {
        return 100 - value * 2 / 3;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return (100 - value) * 3 / 2;
    }
}
