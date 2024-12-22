package dev.turtywurty.heatapi.api.base;

public class NoLimitHeatStorage extends SimpleHeatStorage {
    public NoLimitHeatStorage(boolean insert, boolean extract) {
        super(Long.MAX_VALUE, insert ? Long.MAX_VALUE : 0, extract ? Long.MAX_VALUE : 0);
    }
}
