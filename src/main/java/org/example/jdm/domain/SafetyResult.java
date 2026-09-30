package org.example.jdm.domain;

import java.util.List;
public record SafetyResult(List<String> issues, double thermalLoadKj, double brakingDistanceMeters) {
    public SafetyResult { issues = List.copyOf(issues); }
    public boolean safe() { return issues.isEmpty(); }
}
