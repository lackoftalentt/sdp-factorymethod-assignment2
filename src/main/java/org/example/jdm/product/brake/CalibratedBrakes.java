package org.example.jdm.product.brake;

import org.example.jdm.family.PlatformFamily;
import org.example.jdm.domain.*;
import org.example.jdm.config.PreparationMode;
public abstract class CalibratedBrakes<F extends PlatformFamily> implements BrakeSystem<F> {
    private final String description;
    private final double brakeForceNewtons;
    private final double heatCapacityKj;
    protected CalibratedBrakes(String description, double brakeForceNewtons, double heatCapacityKj) {
        this.description = description; this.brakeForceNewtons = brakeForceNewtons;
        this.heatCapacityKj = heatCapacityKj;
    }
    public final BrakeSetup configureFor(PreparationMode mode) {
        return new BrakeSetup(switch (mode) { case STREET -> 1.0; case DRIFT -> 1.15; case TRACK -> 1.4; });
    }
    public final double calculateBrakingDistance(BrakeSetup setup, double massKg, double speedKmh, DrivingConditions conditions) {
        if (!Double.isFinite(massKg) || massKg <= 0 || !Double.isFinite(speedKmh) || speedKmh < 0 || speedKmh > 400)
            throw new IllegalArgumentException("Mass must be positive and speed must be in [0, 400].");
        double hotFade = 1 + Math.max(0, conditions.temperatureCelsius() - 25) * 0.008 / setup.coolingMultiplier();
        double deceleration = Math.min(1.25 * 9.81, brakeForceNewtons / massKg) * conditions.surfaceGrip() / hotFade;
        return Math.pow(speedKmh / 3.6, 2) / (2 * deceleration);
    }
    public final boolean validateThermalCapacity(BrakeSetup setup, double thermalLoadKj) {
        if (!Double.isFinite(thermalLoadKj) || thermalLoadKj < 0)
            throw new IllegalArgumentException("Thermal load must be nonnegative.");
        return thermalLoadKj <= thermalCapacityKj(setup);
    }
    public final double thermalCapacityKj(BrakeSetup setup) { return heatCapacityKj * setup.coolingMultiplier(); }
    public final String getBrakeDescription() { return description; }
}
