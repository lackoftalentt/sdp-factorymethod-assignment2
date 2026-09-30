package org.example.jdm.product.suspension;

import org.example.jdm.family.PlatformFamily;
import org.example.jdm.config.PreparationMode;
import org.example.jdm.domain.*;
public interface SuspensionSystem<F extends PlatformFamily> {
    SuspensionSetup configureFor(PreparationMode mode, DriverSkillLevel skill);
    double calculateHandlingScore(SuspensionSetup setup, DrivingConditions conditions);
    String getSuspensionDescription();
}
