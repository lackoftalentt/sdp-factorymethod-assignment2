package org.example.jdm.domain;

public record BrakeSetup(double coolingMultiplier) {
    public BrakeSetup {
        if (!Double.isFinite(coolingMultiplier) || coolingMultiplier < 1 || coolingMultiplier > 2)
            throw new IllegalArgumentException("Brake cooling multiplier must be in [1, 2].");
    }
}
