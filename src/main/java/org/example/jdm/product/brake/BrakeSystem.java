package org.example.jdm.product.brake;

import org.example.jdm.family.PlatformFamily;
import org.example.jdm.config.PreparationMode;
import org.example.jdm.domain.*;
public interface BrakeSystem<F extends PlatformFamily> {
    BrakeSetup configureFor(PreparationMode mode);
    double calculateBrakingDistance(BrakeSetup setup, double massKg, double speedKmh, DrivingConditions conditions);
    boolean validateThermalCapacity(BrakeSetup setup, double thermalLoadKj);
    double thermalCapacityKj(BrakeSetup setup);
    String getBrakeDescription();
}
