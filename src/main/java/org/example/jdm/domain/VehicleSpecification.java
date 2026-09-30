package org.example.jdm.domain;

import java.util.Objects;
public record VehicleSpecification(String name, double massKg, double tractionFactor) {
    public VehicleSpecification {
        Objects.requireNonNull(name, "name");
        if (name.isBlank() || !Double.isFinite(massKg) || massKg <= 0
                || !Double.isFinite(tractionFactor) || tractionFactor <= 0 || tractionFactor > 1.5)
            throw new IllegalArgumentException("Invalid vehicle specification.");
    }
}
