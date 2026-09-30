package org.example.jdm.abstractfactory;

import org.example.jdm.family.MazdaRX7Family;
import org.example.jdm.product.ecu.*;
import org.example.jdm.product.suspension.*;
import org.example.jdm.product.brake.*;
import org.example.jdm.domain.VehicleSpecification;
public final class MazdaRX7Factory implements CarPlatformFactory<MazdaRX7Family> {
    public EngineControlUnit<MazdaRX7Family> createEngineControlUnit() { return new MazdaRX7ECU(); }
    public SuspensionSystem<MazdaRX7Family> createSuspensionSystem() { return new MazdaRX7Suspension(); }
    public BrakeSystem<MazdaRX7Family> createBrakeSystem() { return new MazdaRX7Brakes(); }
    public VehicleSpecification specification() { return new VehicleSpecification("Mazda RX-7 FD", 1270, 1.02); }
}
