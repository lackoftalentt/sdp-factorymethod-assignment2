package org.example.jdm.factorymethod;

import org.example.jdm.config.PreparationMode;
import org.example.jdm.domain.*;
public final class DriftPreparationPlan implements PreparationPlan {
    public PreparationMode mode() { return PreparationMode.DRIFT; }
    public double boostFraction(DriverRequirements requirements) {
        return .75 * (requirements.skillLevel() == DriverSkillLevel.NOVICE ? 0.65 : 1.0);
    }
    public void validate(DriverRequirements requirements) { if (requirements.skillLevel() == org.example.jdm.domain.DriverSkillLevel.NOVICE)
            throw new IllegalArgumentException("Drift preparation requires at least intermediate driver skill."); }
    public String name() { return "Drift preparation"; }
}
