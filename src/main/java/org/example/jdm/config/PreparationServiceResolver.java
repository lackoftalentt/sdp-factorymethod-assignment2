package org.example.jdm.config;

import org.example.jdm.factorymethod.*;
/** Runtime wiring, not the Factory Method pattern itself. */
public final class PreparationServiceResolver {
    public BuildPreparationService resolve(PreparationMode mode) {
        return switch (mode) {
            case STREET -> new StreetBuildPreparationService();
            case DRIFT -> new DriftBuildPreparationService();
            case TRACK -> new TrackBuildPreparationService();
        };
    }
}
