package org.example.jdm.domain;

import java.util.List;
import org.example.jdm.family.PlatformFamily;
public record BuildReport<F extends PlatformFamily>(CarBuild<F> build, String planName,
        List<String> preparationNotes, PerformanceResult performance) {
    public BuildReport { preparationNotes = List.copyOf(preparationNotes); }
    public SafetyResult safety() { return performance.safety(); }
}
