package org.example.jdm.domain;

import org.example.jdm.config.PreparationMode;
import java.util.Objects;
public record SuspensionSetup(PreparationMode mode, double rideHeightMm, double gripMultiplier) {
    public SuspensionSetup {
        Objects.requireNonNull(mode, "mode");
        if (!Double.isFinite(rideHeightMm) || rideHeightMm < 60 || rideHeightMm > 200
                || !Double.isFinite(gripMultiplier) || gripMultiplier <= 0)
            throw new IllegalArgumentException("Invalid suspension setup.");
    }
}
