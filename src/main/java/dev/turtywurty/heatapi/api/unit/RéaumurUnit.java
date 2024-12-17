package dev.turtywurty.heatapi.api.unit;

public class RéaumurUnit implements HeatUnit {
    @Override
    public String getUnitName() {
        return "Réaumur";
    }

    @Override
    public String getUnitSymbol() {
        return "°Ré";
    }

    @Override
    public double convertToBaseUnit(double value) {
        return value * 1.25;
    }

    @Override
    public double convertFromBaseUnit(double value) {
        return value / 1.25;
    }
}
