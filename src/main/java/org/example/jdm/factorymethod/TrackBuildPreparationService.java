package org.example.jdm.factorymethod;

public final class TrackBuildPreparationService extends BuildPreparationService {
    @Override
    protected PreparationPlan createPreparationPlan() { return new TrackPreparationPlan(); }
}
