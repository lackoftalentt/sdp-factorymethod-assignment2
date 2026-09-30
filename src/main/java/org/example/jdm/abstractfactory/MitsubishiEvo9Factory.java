package org.example.jdm.abstractfactory;

import org.example.jdm.family.MitsubishiEvo9Family;
import org.example.jdm.product.ecu.*;
import org.example.jdm.product.suspension.*;
import org.example.jdm.product.brake.*;
import org.example.jdm.domain.VehicleSpecification;
public final class MitsubishiEvo9Factory implements CarPlatformFactory<MitsubishiEvo9Family> {
    public EngineControlUnit<MitsubishiEvo9Family> createEngineControlUnit() { return new MitsubishiEvo9ECU(); }
    public SuspensionSystem<MitsubishiEvo9Family> createSuspensionSystem() { return new MitsubishiEvo9Suspension(); }
    public BrakeSystem<MitsubishiEvo9Family> createBrakeSystem() { return new MitsubishiEvo9Brakes(); }
    public VehicleSpecification specification() { return new VehicleSpecification("Mitsubishi Lancer Evolution IX", 1420, 1.16); }
}
