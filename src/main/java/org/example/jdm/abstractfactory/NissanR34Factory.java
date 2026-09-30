package org.example.jdm.abstractfactory;

import org.example.jdm.family.NissanR34Family;
import org.example.jdm.product.ecu.*;
import org.example.jdm.product.suspension.*;
import org.example.jdm.product.brake.*;
import org.example.jdm.domain.VehicleSpecification;
public final class NissanR34Factory implements CarPlatformFactory<NissanR34Family> {
    public EngineControlUnit<NissanR34Family> createEngineControlUnit() { return new NissanR34ECU(); }
    public SuspensionSystem<NissanR34Family> createSuspensionSystem() { return new NissanR34Suspension(); }
    public BrakeSystem<NissanR34Family> createBrakeSystem() { return new NissanR34Brakes(); }
    public VehicleSpecification specification() { return new VehicleSpecification("Nissan Skyline GT-R R34", 1560, 1.12); }
}
