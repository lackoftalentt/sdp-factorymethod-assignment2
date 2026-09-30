package org.example.jdm.config;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.jdm.abstractfactory.*;
import org.example.jdm.config.*;
import org.example.jdm.domain.*;
import org.example.jdm.factorymethod.*;
import org.example.jdm.family.*;
import org.example.jdm.product.ecu.*;
import org.example.jdm.product.suspension.*;
import org.example.jdm.product.brake.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import org.example.jdm.app.*;
class RuntimeSelectionTest {
    @Test void originalNamesResolveToCorrectFactories() {
        var resolver = new CarPlatformFactoryResolver();
        assertInstanceOf(NissanR34Factory.class, resolver.resolve(VehiclePlatform.parse("NISSAN")));
        assertInstanceOf(ToyotaSupraFactory.class, resolver.resolve(VehiclePlatform.parse("toyota")));
        assertInstanceOf(MazdaRX7Factory.class, resolver.resolve(VehiclePlatform.parse("mazda")));
    }
    @Test void unsupportedFamilyAndModeProduceCleanCliErrors() {
        for (String argument : new String[]{"--family=honda", "--mode=rally"}) {
            var errors = new ByteArrayOutputStream();
            int code = new ConsoleApplication().run(new String[]{argument}, System.out, new PrintStream(errors));
            assertEquals(1, code);
            assertTrue(errors.toString(StandardCharsets.UTF_8).contains("Unsupported"));
            assertTrue(errors.toString(StandardCharsets.UTF_8).contains("Usage:"));
        }
    }
    @Test void malformedDuplicateAndInvalidNumericArgumentsAreRejected() {
        for (String[] args : new String[][]{{"nissan"}, {"--unknown=x"}, {"--hp=abc"}, {"--hp=NaN"},
                {"--family="}, {"--wet=perhaps"}, {"--skill=champion"}, {"--hp=300", "--hp=400"}})
            assertThrows(IllegalArgumentException.class, () -> CommandLineOptions.parse(args));
    }
}
