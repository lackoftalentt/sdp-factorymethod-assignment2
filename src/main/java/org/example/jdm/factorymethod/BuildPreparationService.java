package org.example.jdm.factorymethod;

import java.util.List;
import java.util.Objects;
import org.example.jdm.abstractfactory.CarPlatformFactory;
import org.example.jdm.family.PlatformFamily;
import org.example.jdm.domain.*;
import org.example.jdm.service.*;

/** Creator: owns the workflow; subclasses decide only which plan it creates. */
public abstract class BuildPreparationService {
    protected abstract PreparationPlan createPreparationPlan();

    public final <F extends PlatformFamily> BuildReport<F> prepare(CarPlatformFactory<F> factory,
            DriverRequirements requirements, DrivingConditions conditions) {
        Objects.requireNonNull(factory, "factory");
        Objects.requireNonNull(requirements, "requirements");
        Objects.requireNonNull(conditions, "conditions");
        PreparationPlan plan = createPreparationPlan(); // Factory Method: dynamic dispatch to a subclass.
        plan.validate(requirements);
        var ecu = factory.createEngineControlUnit(); // Abstract Factory: one coherent family.
        var suspension = factory.createSuspensionSystem();
        var brakes = factory.createBrakeSystem();
        var tune = ecu.tuneEngine(requirements.desiredHorsepower(), plan.boostFraction(requirements));
        var suspensionSetup = suspension.configureFor(plan.mode(), requirements.skillLevel());
        var brakeSetup = brakes.configureFor(plan.mode());
        var build = new CarBuild<>(factory.specification(), ecu, suspension, brakes, tune,
                suspensionSetup, brakeSetup, plan.mode(), requirements);
        var simulation = new TrackSimulationService(new SafetyValidationService())
                .simulateTrackSession(build, conditions);
        var notes = List.of(tune.limited() ? "Power request limited by the plan and safe boost envelope."
                        : "Requested power achieved within the safe boost envelope.",
                simulation.safety().safe() ? "Workshop checks passed." : "Workshop checks failed; review safety issues.");
        return new BuildReport<>(build, plan.name(), notes, simulation);
    }
}
