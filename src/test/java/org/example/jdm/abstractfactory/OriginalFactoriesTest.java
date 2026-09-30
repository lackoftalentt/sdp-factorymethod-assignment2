package org.example.jdm.abstractfactory;

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
class OriginalFactoriesTest {
    @Test void nissanCreatesThreeCoherentProducts() {
        var factory = new NissanR34Factory();
        assertAll(() -> assertInstanceOf(NissanR34ECU.class, factory.createEngineControlUnit()),
                () -> assertInstanceOf(NissanR34Suspension.class, factory.createSuspensionSystem()),
                () -> assertInstanceOf(NissanR34Brakes.class, factory.createBrakeSystem()));
    }
    @Test void toyotaCreatesThreeCoherentProducts() {
        var factory = new ToyotaSupraFactory();
        assertAll(() -> assertInstanceOf(ToyotaSupraECU.class, factory.createEngineControlUnit()),
                () -> assertInstanceOf(ToyotaSupraSuspension.class, factory.createSuspensionSystem()),
                () -> assertInstanceOf(ToyotaSupraBrakes.class, factory.createBrakeSystem()));
    }
    @Test void mazdaCreatesThreeCoherentProducts() {
        var factory = new MazdaRX7Factory();
        assertAll(() -> assertInstanceOf(MazdaRX7ECU.class, factory.createEngineControlUnit()),
                () -> assertInstanceOf(MazdaRX7Suspension.class, factory.createSuspensionSystem()),
                () -> assertInstanceOf(MazdaRX7Brakes.class, factory.createBrakeSystem()));
    }
    @Test void factoryBuildRetainsItsFamilyThroughTheGenericWorkflow() {
        BuildReport<NissanR34Family> report = new TrackBuildPreparationService().prepare(new NissanR34Factory(),
                new DriverRequirements(400, DriverSkillLevel.INTERMEDIATE, .7), new DrivingConditions(25, 3.5, false));
        CarBuild<NissanR34Family> build = report.build();
        assertAll(() -> assertInstanceOf(NissanR34ECU.class, build.ecu()),
                () -> assertInstanceOf(NissanR34Suspension.class, build.suspension()),
                () -> assertInstanceOf(NissanR34Brakes.class, build.brakes()),
                () -> assertTrue(report.safety().safe()));
    }
}
