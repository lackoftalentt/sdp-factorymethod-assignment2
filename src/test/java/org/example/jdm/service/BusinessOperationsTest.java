package org.example.jdm.service;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.example.jdm.abstractfactory.*;
import org.example.jdm.config.*;
import org.example.jdm.domain.*;
import org.example.jdm.factorymethod.*;
import org.example.jdm.family.*;
import org.example.jdm.product.ecu.*;
import org.example.jdm.product.suspension.*;
import org.example.jdm.product.brake.*;
import org.example.jdm.service.*;
class BusinessOperationsTest {
    private final TrackBuildPreparationService creator = new TrackBuildPreparationService();
    private final DrivingConditions dry = new DrivingConditions(25, 3.5, false);
    private DriverRequirements demand(double power) { return new DriverRequirements(power, DriverSkillLevel.INTERMEDIATE, .7); }
    @Test void prepareVehicleProducesConfiguredBuildAndUsefulPerformanceReport() {
        var report = creator.prepare(new NissanR34Factory(), demand(400), dry);
        assertEquals(400, report.build().tune().horsepower(), .001);
        assertTrue(report.safety().safe());
        assertTrue(report.performance().estimatedLapSeconds() > 60 && report.performance().estimatedLapSeconds() < 300);
        assertEquals(115, report.performance().handlingScore(), .001);
        assertFalse(report.preparationNotes().isEmpty());
    }
    @Test void safetyValidationDetectsHeatOverloadAndBrakeCoolingChangesTheOutcome() {
        var street = new StreetBuildPreparationService().prepare(new NissanR34Factory(), demand(400), dry);
        var track = creator.prepare(new NissanR34Factory(), demand(400), dry);
        var hotSession = new DrivingConditions(40, 5, false);
        var service = new SafetyValidationService();
        var streetSafety = service.performSafetyValidation(street.build(), hotSession);
        var trackSafety = service.performSafetyValidation(track.build(), hotSession);
        assertFalse(streetSafety.safe());
        assertTrue(streetSafety.issues().stream().anyMatch(s -> s.contains("thermal")));
        assertTrue(trackSafety.safe());
        assertEquals(streetSafety.thermalLoadKj(), trackSafety.thermalLoadKj(), .001);
    }
    @Test void simulationRespondsToPowerSuspensionAndWetConditions() {
        var lowPower = creator.prepare(new NissanR34Factory(), demand(300), dry);
        var highPower = creator.prepare(new NissanR34Factory(), demand(400), dry);
        var street = new StreetBuildPreparationService().prepare(new NissanR34Factory(), demand(400), dry);
        var wet = new TrackSimulationService(new SafetyValidationService()).simulateTrackSession(highPower.build(),
                new DrivingConditions(25, 3.5, true));
        assertTrue(highPower.performance().estimatedLapSeconds() < lowPower.performance().estimatedLapSeconds());
        assertTrue(highPower.performance().handlingScore() > street.performance().handlingScore());
        assertTrue(highPower.performance().estimatedLapSeconds() < street.performance().estimatedLapSeconds());
        assertTrue(wet.estimatedLapSeconds() > highPower.performance().estimatedLapSeconds());
        assertTrue(wet.safety().brakingDistanceMeters() > highPower.safety().brakingDistanceMeters());
    }
    @Test void oneUnchangedClientWorksWithEveryRegisteredFactory() {
        var resolver = new CarPlatformFactoryResolver();
        for (VehiclePlatform platform : VehiclePlatform.values()) {
            CarPlatformFactory<? extends PlatformFamily> factory = resolver.resolve(platform);
            var report = creator.prepare(factory, demand(400), dry);
            assertTrue(report.safety().safe(), platform.toString());
            assertEquals(PreparationMode.TRACK, report.build().mode());
            assertTrue(Double.isFinite(report.performance().estimatedLapSeconds()));
        }
    }
    @Test void invalidRequirementsAndDrivingConditionsAreRejected() {
        assertAll(() -> assertThrows(IllegalArgumentException.class, () -> demand(0)),
                () -> assertThrows(IllegalArgumentException.class, () -> demand(1501)),
                () -> assertThrows(IllegalArgumentException.class, () -> demand(Double.NaN)),
                () -> assertThrows(IllegalArgumentException.class, () -> new DriverRequirements(400, DriverSkillLevel.EXPERT, 1.1)),
                () -> assertThrows(IllegalArgumentException.class, () -> new DrivingConditions(25, 0, false)),
                () -> assertThrows(IllegalArgumentException.class, () -> new DrivingConditions(80, 3, false)));
    }
}
