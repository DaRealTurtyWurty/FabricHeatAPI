package dev.turtywurty.heatapi.testmod;

import dev.turtywurty.heatapi.impl.HeatImpl;
import net.minecraft.Bootstrap;
import net.minecraft.SharedConstants;
import org.junit.jupiter.api.BeforeAll;

public class HeatApiTests {
    @BeforeAll
    public static void setup() {
        SharedConstants.createGameVersion();
        Bootstrap.initialize();
        HeatImpl.init();
    }
}
