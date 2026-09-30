package org.example.jdm.product;

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
class ProductBehaviorTest {
    @Test void engineCalibrationLimitsBoostAndModelsRotaryHeatSensitivity() {
        var nissan = new NissanR34ECU(); var toyota = new ToyotaSupraECU(); var mazda = new MazdaRX7ECU();
        var nt = nissan.tuneEngine(1000, 1); var tt = toyota.tuneEngine(1000, 1); var mt = mazda.tuneEngine(1000, 1);
        assertAll(() -> assertEquals(568, nt.horsepower(), .001),
                () -> assertEquals(676, tt.horsepower(), .001),
                () -> assertEquals(431, mt.horsepower(), .001),
                () -> assertEquals(nissan.getSafeBoostLimit(), nt.boostBar()),
                () -> assertTrue(nt.limited() && tt.limited() && mt.limited()));
        var equalTune = new EngineTune(400, .8, false);
        assertTrue(mazda.calculatePower(equalTune, 40) < nissan.calculatePower(equalTune, 40));
        assertTrue(nissan.calculatePower(equalTune, 40) < toyota.calculatePower(equalTune, 40));
    }
    @Test void lightweightMazdaHandlesBestWhileAwdRetainsMoreWetGrip() {
        var nissan = new NissanR34Suspension(); var toyota = new ToyotaSupraSuspension(); var mazda = new MazdaRX7Suspension();
        var ns = nissan.configureFor(PreparationMode.TRACK, DriverSkillLevel.EXPERT);
        var ts = toyota.configureFor(PreparationMode.TRACK, DriverSkillLevel.EXPERT);
        var ms = mazda.configureFor(PreparationMode.TRACK, DriverSkillLevel.EXPERT);
        var dry = new DrivingConditions(25, 3.5, false); var wet = new DrivingConditions(25, 3.5, true);
        assertTrue(mazda.calculateHandlingScore(ms, dry) > nissan.calculateHandlingScore(ns, dry));
        assertTrue(nissan.calculateHandlingScore(ns, wet) / nissan.calculateHandlingScore(ns, dry)
                > toyota.calculateHandlingScore(ts, wet) / toyota.calculateHandlingScore(ts, dry));
        assertTrue(mazda.calculateHandlingScore(ms, wet) < mazda.calculateHandlingScore(ms, dry));
    }
    @Test void brakeFamiliesDifferUnderIdenticalMassAndThermalLoad() {
        var toyota = new ToyotaSupraBrakes(); var mazda = new MazdaRX7Brakes();
        var setup = new BrakeSetup(1); var conditions = new DrivingConditions(25, 3.5, false);
        assertTrue(toyota.calculateBrakingDistance(setup, 1500, 100, conditions)
                < mazda.calculateBrakingDistance(setup, 1500, 100, conditions));
        assertTrue(toyota.validateThermalCapacity(setup, 2200));
        assertFalse(mazda.validateThermalCapacity(setup, 2200));
        assertTrue(mazda.validateThermalCapacity(mazda.configureFor(PreparationMode.TRACK), 2200));
    }
}
