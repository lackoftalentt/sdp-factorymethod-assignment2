package org.example.jdm.product.suspension;

import org.example.jdm.family.PlatformFamily;
import org.example.jdm.domain.*;
import org.example.jdm.config.PreparationMode;
import java.util.Objects;
public abstract class CalibratedSuspension<F extends PlatformFamily> implements SuspensionSystem<F> {
    private final String description;
    private final double baseHandling;
    private final double streetHeight;
    private final double driftGrip;
    private final double wetRetention;
    protected CalibratedSuspension(String description, double baseHandling, double streetHeight,
            double driftGrip, double wetRetention) {
        this.description = description; this.baseHandling = baseHandling; this.streetHeight = streetHeight;
        this.driftGrip = driftGrip; this.wetRetention = wetRetention;
    }
    public final SuspensionSetup configureFor(PreparationMode mode, DriverSkillLevel skill) {
        Objects.requireNonNull(skill, "skill");
        double grip = switch (mode) { case STREET -> 1.0; case DRIFT -> driftGrip; case TRACK -> 1.15; };
        double drop = switch (mode) { case STREET -> 0; case DRIFT -> 15; case TRACK -> 25; };
        return new SuspensionSetup(mode, streetHeight - drop, grip * (skill == DriverSkillLevel.NOVICE ? 0.95 : 1));
    }
    public final double calculateHandlingScore(SuspensionSetup setup, DrivingConditions conditions) {
        return baseHandling * setup.gripMultiplier() * conditions.surfaceGrip()
                * (conditions.wet() ? wetRetention : 1);
    }
    public final String getSuspensionDescription() { return description; }
}
