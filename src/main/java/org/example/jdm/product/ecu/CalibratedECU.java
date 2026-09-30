package org.example.jdm.product.ecu;

import org.example.jdm.family.PlatformFamily;
import org.example.jdm.domain.EngineTune;

/** Shared calibration formula; concrete platforms supply different engine envelopes. */
public abstract class CalibratedECU<F extends PlatformFamily> implements EngineControlUnit<F> {
    private final String description;
    private final double basePower;
    private final double horsepowerPerBar;
    private final double safeBoost;
    private final double heatSensitivity;
    protected CalibratedECU(String description, double basePower, double horsepowerPerBar,
            double safeBoost, double heatSensitivity) {
        this.description = description; this.basePower = basePower;
        this.horsepowerPerBar = horsepowerPerBar; this.safeBoost = safeBoost;
        this.heatSensitivity = heatSensitivity;
    }
    public final EngineTune tuneEngine(double desiredHorsepower, double boostFraction) {
        if (!Double.isFinite(desiredHorsepower) || desiredHorsepower <= 0
                || !Double.isFinite(boostFraction) || boostFraction <= 0 || boostFraction > 1)
            throw new IllegalArgumentException("Power must be positive and boost fraction must be in (0, 1].");
        double boost = Math.min(safeBoost * boostFraction, Math.max(0, (desiredHorsepower - basePower) / horsepowerPerBar));
        double horsepower = Math.min(desiredHorsepower, basePower + boost * horsepowerPerBar);
        return new EngineTune(horsepower, boost, horsepower + 0.001 < desiredHorsepower);
    }
    public final double getSafeBoostLimit() { return safeBoost; }
    public final double calculatePower(EngineTune tune, double temperatureCelsius) {
        if (!Double.isFinite(temperatureCelsius) || temperatureCelsius < -20 || temperatureCelsius > 55)
            throw new IllegalArgumentException("Temperature outside supported range.");
        return tune.horsepower() * (1 - Math.max(0, temperatureCelsius - 20) * heatSensitivity);
    }
    public final String getEngineDescription() { return description; }
}
