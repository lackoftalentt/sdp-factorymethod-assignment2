package org.example.jdm.factorymethod;

public final class StreetBuildPreparationService extends BuildPreparationService {
    @Override
    protected PreparationPlan createPreparationPlan() { return new StreetPreparationPlan(); }
}
