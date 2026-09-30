package org.example.jdm.config;

import org.example.jdm.abstractfactory.*;
import org.example.jdm.family.PlatformFamily;
/** Composition root: the only production location selecting concrete platform factories. */
public final class CarPlatformFactoryResolver {
    public CarPlatformFactory<? extends PlatformFamily> resolve(VehiclePlatform platform) {
        return switch (platform) {
            case NISSAN -> new NissanR34Factory();
            case TOYOTA -> new ToyotaSupraFactory();
            case MAZDA -> new MazdaRX7Factory();
            case MITSUBISHI -> new MitsubishiEvo9Factory();
        };
    }
}
