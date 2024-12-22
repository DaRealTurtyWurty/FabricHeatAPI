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

    /**
     * Gets the room temperature in the specified unit.
     *
     * @param unit The unit to get the room temperature in
     * @return The room temperature in the specified unit
     */
    static double getRoomTemperature(HeatUnit unit) {
        return unit.convertFromBaseUnit(21);
    }

    /**
     * Gets the name of this unit.
     *
     * @return The name of this unit
     */
    String getUnitName();

    /**
     * Gets the symbol of this unit.
     *
     * @return The symbol of this unit
     */
    String getUnitSymbol();

    /**
     * Converts the specified value to the base unit.
     *
     * @param value The value to convert
     * @return The value in the base unit
     * @implNote The base unit is {@link HeatUnit#CELSIUS}
     * @see HeatUnit#convertFromUnit(double, HeatUnit)
     * @see HeatUnit#convertToUnit(double, HeatUnit)
     */
    double convertToBaseUnit(double value);

    /**
     * Converts the specified value from the base unit.
     *
     * @param value The value to convert
     * @return The value from the base unit
     * @implNote The base unit is {@link HeatUnit#CELSIUS}
     * @see HeatUnit#convertToUnit(double, HeatUnit)
     * @see HeatUnit#convertFromUnit(double, HeatUnit)
     */
    double convertFromBaseUnit(double value);

    /**
     * Gets the maximum value of this unit.
     *
     * @return The maximum value of this unit
     */
    default double getMinValue() {
        return HeatUnit.KELVIN.convertToUnit(0, this);
    }

    /**
     * Converts the specified value to the specified unit.
     *
     * @param value The value to convert
     * @param unit  The unit to convert to
     * @return The value in the specified unit
     * @see HeatUnit#convertToBaseUnit(double)
     * @see HeatUnit#convertFromBaseUnit(double)
     */
    default double convertToUnit(double value, HeatUnit unit) {
        return unit.convertFromBaseUnit(convertToBaseUnit(value));
    }

    /**
     * Converts the specified value from the specified unit.
     *
     * @param value The value to convert
     * @param unit  The unit to convert from
     * @return The value from the specified unit
     * @see HeatUnit#convertFromBaseUnit(double)
     * @see HeatUnit#convertToBaseUnit(double)
     */
    default double convertFromUnit(double value, HeatUnit unit) {
        return convertFromBaseUnit(unit.convertToBaseUnit(value));
    }
}
