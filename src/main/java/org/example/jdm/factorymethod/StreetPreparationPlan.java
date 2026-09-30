package org.example.jdm.factorymethod;

import org.example.jdm.config.PreparationMode;
import org.example.jdm.domain.*;
public final class StreetPreparationPlan implements PreparationPlan {
    public PreparationMode mode() { return PreparationMode.STREET; }
    public double boostFraction(DriverRequirements requirements) {
        return .55 * (requirements.skillLevel() == DriverSkillLevel.NOVICE ? 0.65 : 1.0);
    }
    public void validate(DriverRequirements requirements) { java.util.Objects.requireNonNull(requirements, "requirements"); }
    public String name() { return "Street preparation"; }
}
