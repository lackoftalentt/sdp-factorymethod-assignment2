package org.example.jdm.service;

import java.util.ArrayList;
import org.example.jdm.domain.*;
import org.example.jdm.config.PreparationMode;

public final class SafetyValidationService {
    public SafetyResult performSafetyValidation(CarBuild<?> build, DrivingConditions conditions) {
        double power = build.ecu().calculatePower(build.tune(), conditions.temperatureCelsius());
        double handling = build.suspension().calculateHandlingScore(build.suspensionSetup(), conditions);
        double speedKmh = 100;
        double stoppingDistance = build.brakes().calculateBrakingDistance(build.brakeSetup(),
                build.specification().massKg(), speedKmh, conditions);
        // Heat proxy: one 140 km/h stop plus repeated engine work over the session.
        double thermalLoad = 0.5 * build.specification().massKg() * Math.pow(140 / 3.6, 2) / 1000
                + power * conditions.trackLengthKm() * build.requirements().usageIntensity() * 0.7;
        thermalLoad *= 1 + Math.max(0, conditions.temperatureCelsius() - 25) * 0.01;
        var issues = new ArrayList<String>();
        if (!build.brakes().validateThermalCapacity(build.brakeSetup(), thermalLoad))
            issues.add("Brake thermal capacity exceeded; reduce session intensity or duration.");
        if (stoppingDistance > (conditions.wet() ? 75 : 55))
            issues.add("Stopping distance exceeds the workshop limit.");
        if (build.suspensionSetup().mode() != build.mode())
            issues.add("Suspension setup does not match preparation mode.");
        double controllablePower = build.specification().massKg() * handling / 100
                * (build.requirements().skillLevel() == DriverSkillLevel.NOVICE ? 0.25 : 0.60);
        if (power > controllablePower)
            issues.add("Power exceeds the handling allowance for this driver and surface.");
        if (build.mode() == PreparationMode.DRIFT && build.requirements().skillLevel() == DriverSkillLevel.NOVICE)
            issues.add("Drift preparation requires at least intermediate driver skill.");
        return new SafetyResult(issues, thermalLoad, stoppingDistance);
    }
}
