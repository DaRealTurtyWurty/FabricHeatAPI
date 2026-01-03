package dev.turtywurty.heatapi.testmod;

import dev.turtywurty.heatapi.impl.HeatImpl;
import net.minecraft.server.Bootstrap;
import net.minecraft.SharedConstants;
import org.junit.jupiter.api.BeforeAll;

public class HeatApiTests {
    @BeforeAll
    public static void setup() {
        SharedConstants.tryDetectVersion();
        Bootstrap.bootStrap();
        HeatImpl.init();
    }
}
