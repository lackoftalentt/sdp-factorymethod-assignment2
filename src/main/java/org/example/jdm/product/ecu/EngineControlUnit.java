package org.example.jdm.product.ecu;

import org.example.jdm.family.PlatformFamily;
import org.example.jdm.domain.EngineTune;
public interface EngineControlUnit<F extends PlatformFamily> {
    EngineTune tuneEngine(double desiredHorsepower, double boostFraction);
    double getSafeBoostLimit();
    double calculatePower(EngineTune tune, double temperatureCelsius);
    String getEngineDescription();
}
