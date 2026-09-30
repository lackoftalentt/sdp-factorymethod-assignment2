package org.example.jdm.app;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import org.junit.jupiter.api.Test;
import org.example.jdm.config.PreparationMode;
import org.example.jdm.config.VehiclePlatform;
import static org.junit.jupiter.api.Assertions.*;

class ConsoleApplicationTest {
    @Test void allRegisteredCliCombinationsProduceReportsAndUnsafeSessionsReturnStatusTwo() {
        var application = new ConsoleApplication();
        for (VehiclePlatform family : VehiclePlatform.values()) {
            for (PreparationMode mode : PreparationMode.values()) {
                var output = new ByteArrayOutputStream();
                var errors = new ByteArrayOutputStream();
                int status = application.run(new String[]{"--family=" + family.name().toLowerCase(Locale.ROOT),
                        "--mode=" + mode.name().toLowerCase(Locale.ROOT)}, new PrintStream(output), new PrintStream(errors));
                String report = output.toString(StandardCharsets.UTF_8);
                assertEquals(0, status, family + "/" + mode + ": " + report);
                assertEquals("", errors.toString(StandardCharsets.UTF_8));
                assertTrue(report.contains("Preparation mode: " + mode));
                assertTrue(report.contains("Safety validation: PASS"));
                assertTrue(report.contains("Final performance score:"));
            }
        }
        var output = new ByteArrayOutputStream();
        int status = application.run(new String[]{"--family=mazda", "--mode=track", "--length=30",
                "--intensity=1", "--temperature=55"}, new PrintStream(output), System.err);
        assertEquals(2, status);
        assertTrue(output.toString(StandardCharsets.UTF_8).contains("Brake thermal capacity exceeded"));
        assertTrue(output.toString(StandardCharsets.UTF_8).contains("Safety validation: FAIL"));
    }
}
