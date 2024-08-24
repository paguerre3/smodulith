package org.smodulith;

import org.junit.jupiter.api.Test;
import org.springframework.modulith.core.ApplicationModules;
import org.springframework.modulith.docs.Documenter;

public class ModularityTests {
    ApplicationModules modules = ApplicationModules.of(SmodulithApplication.class);

    @Test
    void verify() {
        System.out.println(modules);
        modules.verify();
        System.out.println("###Modules verification passed###");
    }

    @Test
    void renderDocumentation() throws Exception {
        var canvasOptions = Documenter.CanvasOptions.defaults()
                // --> Optionally enable linking of JavaDoc
                // .withApiBase("https://foobar.something")
        ;

        var docOptions = Documenter.DiagramOptions.defaults()
                .withStyle(Documenter.DiagramOptions.DiagramStyle.UML);


        // Document PlantUML diagrams:
        new Documenter(modules) //
                .writeDocumentation(docOptions, canvasOptions);
        System.out.println("###Modules PlantUML Documentation generated###");
    }
}
