package org.example.jdm.config;

public enum PreparationMode {
    STREET, DRIFT, TRACK;
    public static PreparationMode parse(String value) {
        try { return valueOf(value.toUpperCase(java.util.Locale.ROOT)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("Unsupported mode: " + value + ". Use street, drift or track."); }
    }
}
