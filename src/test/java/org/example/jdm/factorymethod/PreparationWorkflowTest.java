package org.example.jdm.factorymethod;

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
class PreparationWorkflowTest {
    private final DriverRequirements demand = new DriverRequirements(650, DriverSkillLevel.EXPERT, .7);
    private final DrivingConditions conditions = new DrivingConditions(25, 3.5, false);
    @Test void streetCreatorUsesConservativeStreetPlan() {
        var report = new StreetBuildPreparationService().prepare(new NissanR34Factory(), demand, conditions);
        assertEquals("Street preparation", report.planName());
        assertEquals(PreparationMode.STREET, report.build().suspensionSetup().mode());
        assertEquals(438.4, report.build().tune().horsepower(), .001);
        assertEquals(135, report.build().suspensionSetup().rideHeightMm());
    }
    @Test void driftCreatorUsesDriftPlanAndRejectsNovices() {
        var creator = new DriftBuildPreparationService();
        var report = creator.prepare(new NissanR34Factory(), demand, conditions);
        assertEquals("Drift preparation", report.planName());
        assertEquals(PreparationMode.DRIFT, report.build().suspensionSetup().mode());
        assertEquals(496, report.build().tune().horsepower(), .001);
        assertThrows(IllegalArgumentException.class, () -> creator.prepare(new NissanR34Factory(),
                new DriverRequirements(300, DriverSkillLevel.NOVICE, .5), conditions));
    }
    @Test void trackCreatorUsesFullEnvelopeAndTrackCooling() {
        var report = new TrackBuildPreparationService().prepare(new NissanR34Factory(), demand, conditions);
        assertEquals("Track preparation", report.planName());
        assertEquals(PreparationMode.TRACK, report.build().suspensionSetup().mode());
        assertEquals(568, report.build().tune().horsepower(), .001);
        assertEquals(3220, report.build().brakes().thermalCapacityKj(report.build().brakeSetup()), .001);
        assertTrue(report.build().tune().limited());
    }
}
