package dev.turtywurty.heatapi.api.unit;

public interface HeatUnit {
    HeatUnit CELSIUS = new CelsiusUnit();
    HeatUnit KELVIN = new KelvinUnit();
    HeatUnit FAHRENHEIT = new FahrenheitUnit();
    HeatUnit RANKINE = new RankineUnit();
    HeatUnit DELISLE = new DelisleUnit();
    HeatUnit NEWTON = new NewtonUnit();
    HeatUnit RÉAUMUR = new RéaumurUnit();
    HeatUnit RØMER = new RømerUnit();

    String getUnitName();
    String getUnitSymbol();

    double convertToBaseUnit(double value);
    double convertFromBaseUnit(double value);

    default double convertToUnit(double value, HeatUnit unit) {
        return unit.convertFromBaseUnit(convertToBaseUnit(value));
    }

    default double convertFromUnit(double value, HeatUnit unit) {
        return convertFromBaseUnit(unit.convertToBaseUnit(value));
    }
}
