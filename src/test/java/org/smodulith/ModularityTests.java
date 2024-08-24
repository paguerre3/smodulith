package org.smodulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;

public class ModularityTests {
    ApplicationModules modules = ApplicationModules.of(SmodulithApplication.class);

    @Test
    void verify() {
        System.out.println(modules);
        modules.verify();
    }
}
