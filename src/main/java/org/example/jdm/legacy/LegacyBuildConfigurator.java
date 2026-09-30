package org.example.jdm.legacy;

import java.util.Locale;

/** Part A: deliberately untyped, direct construction before introducing either pattern. */
public final class LegacyBuildConfigurator {
    public record LegacyECU(String platform, double horsepower) { }
    public record LegacySuspension(String platform, double grip) { }
    public record LegacyBrakes(String platform, double supportedHorsepower) { }
    // No shared family parameter: callers can freely combine incompatible parts.
    public record LegacyBuild(LegacyECU ecu, LegacySuspension suspension, LegacyBrakes brakes) {
        public double performanceScore() {
            return ecu.horsepower() * suspension.grip() / 10;
        }
    }

    public LegacyBuild configure(String platform) {
        return switch (platform.toLowerCase(Locale.ROOT)) {
            case "nissan" -> new LegacyBuild(new LegacyECU("R34", 330),
                    new LegacySuspension("R34", 1.10), new LegacyBrakes("R34", 420));
            case "toyota" -> new LegacyBuild(new LegacyECU("Supra", 360),
                    new LegacySuspension("Supra", 1.02), new LegacyBrakes("Supra", 460));
            case "mazda" -> new LegacyBuild(new LegacyECU("RX-7", 300),
                    new LegacySuspension("RX-7", 1.20), new LegacyBrakes("RX-7", 350));
            default -> throw new IllegalArgumentException("Unsupported legacy platform: " + platform);
        };
    }
}
