package org.example.jdm.abstractfactory;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.jdm.config.*;
import org.example.jdm.domain.*;
import org.example.jdm.factorymethod.TrackBuildPreparationService;
import org.example.jdm.family.MitsubishiEvo9Family;
import org.example.jdm.product.ecu.MitsubishiEvo9ECU;
import org.example.jdm.product.suspension.MitsubishiEvo9Suspension;
import org.example.jdm.product.brake.MitsubishiEvo9Brakes;

class MitsubishiExtensionTest {
    @Test void factoryCreatesAllThreeTypedProductsWithDistinctCalibration() {
        var factory = new MitsubishiEvo9Factory();
        var ecu = factory.createEngineControlUnit();
        var suspension = factory.createSuspensionSystem();
        var brakes = factory.createBrakeSystem();
        assertAll(() -> assertInstanceOf(MitsubishiEvo9ECU.class, ecu),
                () -> assertInstanceOf(MitsubishiEvo9Suspension.class, suspension),
                () -> assertInstanceOf(MitsubishiEvo9Brakes.class, brakes),
                () -> assertEquals(527.5, ecu.tuneEngine(800, 1).horsepower(), .001),
                () -> assertEquals(3500, brakes.thermalCapacityKj(brakes.configureFor(PreparationMode.TRACK)), .001));
        var setup = suspension.configureFor(PreparationMode.TRACK, DriverSkillLevel.EXPERT);
        double wetRetention = suspension.calculateHandlingScore(setup, new DrivingConditions(25, 3.5, true))
                / suspension.calculateHandlingScore(setup, new DrivingConditions(25, 3.5, false));
        assertEquals(.72 * 1.12, wetRetention, .001);
    }
    @Test void runtimeResolverSelectsMitsubishiFactory() {
        var factory = new CarPlatformFactoryResolver().resolve(VehiclePlatform.parse("mitsubishi"));
        assertInstanceOf(MitsubishiEvo9Factory.class, factory);
    }
    @Test void unchangedCreatorBuildsTypedMitsubishiAndSimulatesWetTrackSession() {
        BuildReport<MitsubishiEvo9Family> report = new TrackBuildPreparationService().prepare(
                new MitsubishiEvo9Factory(), new DriverRequirements(420, DriverSkillLevel.INTERMEDIATE, .8),
                new DrivingConditions(30, 4, true));
        assertTrue(report.safety().safe());
        assertEquals(420, report.build().tune().horsepower(), .001);
        assertTrue(report.performance().estimatedLapSeconds() > 0);
        assertInstanceOf(MitsubishiEvo9ECU.class, report.build().ecu());
        assertInstanceOf(MitsubishiEvo9Suspension.class, report.build().suspension());
        assertInstanceOf(MitsubishiEvo9Brakes.class, report.build().brakes());
    }
}
