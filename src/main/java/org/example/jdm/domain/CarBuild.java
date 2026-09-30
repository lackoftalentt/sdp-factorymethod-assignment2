package org.example.jdm.domain;

import java.util.Objects;
import org.example.jdm.family.PlatformFamily;
import org.example.jdm.product.ecu.EngineControlUnit;
import org.example.jdm.product.suspension.SuspensionSystem;
import org.example.jdm.product.brake.BrakeSystem;
import org.example.jdm.config.PreparationMode;
/** Invariant generic arguments require all three components to share one F.
 * Java cannot widen EngineControlUnit<NissanR34Family> to EngineControlUnit<PlatformFamily>.
 * Consequently incompatible products cannot be passed to this constructor in typed Java.
 */
public record CarBuild<F extends PlatformFamily>(VehicleSpecification specification,
        EngineControlUnit<F> ecu, SuspensionSystem<F> suspension, BrakeSystem<F> brakes,
        EngineTune tune, SuspensionSetup suspensionSetup, BrakeSetup brakeSetup,
        PreparationMode mode, DriverRequirements requirements) {
    public CarBuild {
        Objects.requireNonNull(specification); Objects.requireNonNull(ecu);
        Objects.requireNonNull(suspension); Objects.requireNonNull(brakes);
        Objects.requireNonNull(tune); Objects.requireNonNull(suspensionSetup);
        Objects.requireNonNull(brakeSetup); Objects.requireNonNull(mode); Objects.requireNonNull(requirements);
    }
}
