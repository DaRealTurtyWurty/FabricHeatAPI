package dev.turtywurty.heatapi.api.base;

public class NoLimitHeatStorage extends SimpleHeatStorage {
    public NoLimitHeatStorage(boolean insert, boolean extract) {
        super(Double.MAX_VALUE, insert ? Double.MAX_VALUE : 0, extract ? Double.MAX_VALUE : 0);
    }
}
