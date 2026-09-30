package org.example.jdm.domain;

import java.util.Objects;
public record DriverRequirements(double desiredHorsepower, DriverSkillLevel skillLevel, double usageIntensity) {
    public DriverRequirements {
        if (!Double.isFinite(desiredHorsepower) || desiredHorsepower <= 0 || desiredHorsepower > 1500)
            throw new IllegalArgumentException("Desired horsepower must be in (0, 1500].");
        Objects.requireNonNull(skillLevel, "skillLevel");
        if (!Double.isFinite(usageIntensity) || usageIntensity <= 0 || usageIntensity > 1)
            throw new IllegalArgumentException("Usage intensity must be in (0, 1].");
    }
}
