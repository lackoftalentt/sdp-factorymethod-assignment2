package org.example.jdm.app;

import java.io.PrintStream;
import java.util.Locale;
import org.example.jdm.abstractfactory.CarPlatformFactory;
import org.example.jdm.family.PlatformFamily;
import org.example.jdm.config.*;
import org.example.jdm.domain.BuildReport;

public final class ConsoleApplication {
    public int run(String[] args, PrintStream out, PrintStream err) {
        try {
            var options = CommandLineOptions.parse(args);
            var factory = new CarPlatformFactoryResolver().resolve(options.platform());
            // Wildcard capture preserves the selected factory's unknown but consistent F.
            var report = prepareTyped(factory, options);
            printReport(report, out);
            return report.safety().safe() ? 0 : 2;
        } catch (IllegalArgumentException e) {
            err.println("Input error: " + e.getMessage());
            err.println("Usage: --family=<platform> --mode=street|drift|track [--hp=400] [--skill=intermediate]"
                    + " [--intensity=0.7] [--temperature=25] [--length=3.5] [--wet=false]");
            return 1;
        }
    }
    private <F extends PlatformFamily> BuildReport<F> prepareTyped(CarPlatformFactory<F> factory,
            CommandLineOptions options) {
        return new PreparationServiceResolver().resolve(options.mode())
                .prepare(factory, options.requirements(), options.conditions());
    }
    private void printReport(BuildReport<?> report, PrintStream out) {
        var build = report.build();
        var performance = report.performance();
        out.println("JDM Track Build Configuration System");
        out.println("Platform: " + build.specification().name());
        out.println("Preparation mode: " + build.mode() + " (" + report.planName() + ")");
        out.printf(Locale.ROOT, "%nECU: %s%nTuned power: %.1f hp; boost: %.2f bar%n",
                build.ecu().getEngineDescription(), build.tune().horsepower(), build.tune().boostBar());
        out.printf(Locale.ROOT, "Suspension: %s%nRide height: %.0f mm; handling score: %.1f%n",
                build.suspension().getSuspensionDescription(), build.suspensionSetup().rideHeightMm(), performance.handlingScore());
        out.printf(Locale.ROOT, "Brakes: %s%nThermal capacity: %.0f kJ%n", build.brakes().getBrakeDescription(),
                build.brakes().thermalCapacityKj(build.brakeSetup()));
        out.println("\nPreparation results:");
        report.preparationNotes().forEach(out::println);
        out.println("\nSafety validation: " + (report.safety().safe() ? "PASS" : "FAIL"));
        report.safety().issues().forEach(out::println);
        out.printf(Locale.ROOT, "100–0 km/h braking: %.1f m; thermal load: %.0f kJ%n",
                report.safety().brakingDistanceMeters(), report.safety().thermalLoadKj());
        out.printf(Locale.ROOT, "%nTrack simulation:%nEffective power: %.1f hp; estimated lap: %.1f s%nFinal performance score: %.1f%n",
                performance.effectiveHorsepower(), performance.estimatedLapSeconds(), performance.performanceScore());
    }
}
