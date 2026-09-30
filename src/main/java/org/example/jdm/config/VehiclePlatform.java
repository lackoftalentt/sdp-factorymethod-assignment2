package org.example.jdm.config;

public enum VehiclePlatform {
    NISSAN, TOYOTA, MAZDA, MITSUBISHI;
    public static VehiclePlatform parse(String value) {
        try { return valueOf(value.toUpperCase(java.util.Locale.ROOT)); }
        catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Unsupported family: " + value + ". Supported: " + java.util.Arrays.toString(values()));
        }
    }
}
