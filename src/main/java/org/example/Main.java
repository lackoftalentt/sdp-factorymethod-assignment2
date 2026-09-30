package org.example;

import org.example.jdm.legacy.LegacyBuildConfigurator;

public final class Main {
    private Main() { }
    public static void main(String[] args) {
        System.out.println(new LegacyBuildConfigurator().configure("nissan"));
    }
}
