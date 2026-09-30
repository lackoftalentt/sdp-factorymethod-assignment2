package org.example.jdm.domain;

public record PerformanceResult(double effectiveHorsepower, double handlingScore,
        double estimatedLapSeconds, double performanceScore, SafetyResult safety) { }
