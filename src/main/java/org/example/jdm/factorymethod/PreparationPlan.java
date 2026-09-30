package org.example.jdm.factorymethod;

import org.example.jdm.config.PreparationMode;
import org.example.jdm.domain.DriverRequirements;
public interface PreparationPlan {
    PreparationMode mode();
    double boostFraction(DriverRequirements requirements);
    void validate(DriverRequirements requirements);
    String name();
}
