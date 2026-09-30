package org.example.jdm.abstractfactory;

import org.example.jdm.family.ToyotaSupraFamily;
import org.example.jdm.product.ecu.*;
import org.example.jdm.product.suspension.*;
import org.example.jdm.product.brake.*;
import org.example.jdm.domain.VehicleSpecification;
public final class ToyotaSupraFactory implements CarPlatformFactory<ToyotaSupraFamily> {
    public EngineControlUnit<ToyotaSupraFamily> createEngineControlUnit() { return new ToyotaSupraECU(); }
    public SuspensionSystem<ToyotaSupraFamily> createSuspensionSystem() { return new ToyotaSupraSuspension(); }
    public BrakeSystem<ToyotaSupraFamily> createBrakeSystem() { return new ToyotaSupraBrakes(); }
    public VehicleSpecification specification() { return new VehicleSpecification("Toyota Supra MK4", 1490, 1.0); }
}
