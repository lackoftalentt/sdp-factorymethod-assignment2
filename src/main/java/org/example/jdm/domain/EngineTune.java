package org.example.jdm.domain;

public record EngineTune(double horsepower, double boostBar, boolean limited) {
    public EngineTune {
        if (!Double.isFinite(horsepower) || horsepower <= 0 || !Double.isFinite(boostBar) || boostBar < 0)
            throw new IllegalArgumentException("Invalid engine tune.");
    }
}
