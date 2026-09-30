package org.example.jdm.factorymethod;

public final class DriftBuildPreparationService extends BuildPreparationService {
    @Override
    protected PreparationPlan createPreparationPlan() { return new DriftPreparationPlan(); }
}
