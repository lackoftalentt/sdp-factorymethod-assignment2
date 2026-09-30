package org.example.jdm.abstractfactory;

import org.example.jdm.family.PlatformFamily;
import org.example.jdm.product.ecu.EngineControlUnit;
import org.example.jdm.product.suspension.SuspensionSystem;
import org.example.jdm.product.brake.BrakeSystem;
import org.example.jdm.domain.VehicleSpecification;
/** Every creation method carries the SAME F, preserving the family through the client. */
public interface CarPlatformFactory<F extends PlatformFamily> {
    EngineControlUnit<F> createEngineControlUnit();
    SuspensionSystem<F> createSuspensionSystem();
    BrakeSystem<F> createBrakeSystem();
    VehicleSpecification specification();
}
