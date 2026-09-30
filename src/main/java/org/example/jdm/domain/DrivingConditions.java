package org.example.jdm.domain;

public record DrivingConditions(double temperatureCelsius, double trackLengthKm, boolean wet) {
    public DrivingConditions {
        if (!Double.isFinite(temperatureCelsius) || temperatureCelsius < -20 || temperatureCelsius > 55)
            throw new IllegalArgumentException("Temperature must be between -20 and 55 Celsius.");
        if (!Double.isFinite(trackLengthKm) || trackLengthKm <= 0 || trackLengthKm > 30)
            throw new IllegalArgumentException("Track length must be in (0, 30] km.");
    }
    public double surfaceGrip() { return wet ? 0.72 : 1.0; }
}
