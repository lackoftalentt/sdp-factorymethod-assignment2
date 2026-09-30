package org.example.jdm.service;

import java.util.Objects;
import org.example.jdm.domain.*;

/** Educational estimates, calibrated for comparisons rather than real motorsport decisions. */
public final class TrackSimulationService {
    private final SafetyValidationService safetyService;
    public TrackSimulationService(SafetyValidationService safetyService) {
        this.safetyService = Objects.requireNonNull(safetyService);
    }
    public PerformanceResult simulateTrackSession(CarBuild<?> build, DrivingConditions conditions) {
        SafetyResult safety = safetyService.performSafetyValidation(build, conditions);
        double power = build.ecu().calculatePower(build.tune(), conditions.temperatureCelsius());
        double handling = build.suspension().calculateHandlingScore(build.suspensionSetup(), conditions);
        double powerToWeight = power / build.specification().massKg();
        double brakeFactor = Math.sqrt(40 / safety.brakingDistanceMeters());
        double thermalFactor = Math.min(1, build.brakes().thermalCapacityKj(build.brakeSetup()) / safety.thermalLoadKj());
        double averageSpeedKmh = 75 * Math.sqrt(powerToWeight / 0.20) * Math.sqrt(handling / 100)
                * brakeFactor * Math.sqrt(build.specification().tractionFactor()) * thermalFactor;
        double lapSeconds = conditions.trackLengthKm() / averageSpeedKmh * 3600;
        return new PerformanceResult(power, handling, lapSeconds, averageSpeedKmh * handling / 100, safety);
    }
}
