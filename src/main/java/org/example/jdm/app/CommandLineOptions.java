package org.example.jdm.app;

import java.util.HashMap;
import java.util.Locale;
import java.util.Set;
import org.example.jdm.config.*;
import org.example.jdm.domain.*;

public record CommandLineOptions(VehiclePlatform platform, PreparationMode mode,
        DriverRequirements requirements, DrivingConditions conditions) {
    public static CommandLineOptions parse(String[] args) {
        var values = new HashMap<String, String>();
        var allowed = Set.of("family", "mode", "hp", "skill", "intensity", "temperature", "length", "wet");
        for (String arg : args) {
            if (!arg.startsWith("--") || !arg.contains("="))
                throw new IllegalArgumentException("Expected --key=value, received: " + arg);
            String[] pair = arg.substring(2).split("=", 2);
            if (!allowed.contains(pair[0]) || pair[1].isBlank())
                throw new IllegalArgumentException("Unknown or empty option: " + arg);
            if (values.putIfAbsent(pair[0], pair[1]) != null)
                throw new IllegalArgumentException("Duplicate option: --" + pair[0]);
        }
        String wet = values.getOrDefault("wet", "false");
        if (!wet.equalsIgnoreCase("true") && !wet.equalsIgnoreCase("false"))
            throw new IllegalArgumentException("--wet must be true or false.");
        DriverSkillLevel skill;
        try { skill = DriverSkillLevel.valueOf(values.getOrDefault("skill", "intermediate").toUpperCase(Locale.ROOT)); }
        catch (IllegalArgumentException e) { throw new IllegalArgumentException("--skill must be novice, intermediate or expert."); }
        try {
            return new CommandLineOptions(VehiclePlatform.parse(values.getOrDefault("family", "nissan")),
                    PreparationMode.parse(values.getOrDefault("mode", "track")),
                    new DriverRequirements(Double.parseDouble(values.getOrDefault("hp", "400")), skill,
                            Double.parseDouble(values.getOrDefault("intensity", "0.7"))),
                    new DrivingConditions(Double.parseDouble(values.getOrDefault("temperature", "25")),
                            Double.parseDouble(values.getOrDefault("length", "3.5")), Boolean.parseBoolean(wet)));
        } catch (NumberFormatException e) { throw new IllegalArgumentException("Numeric options must contain valid numbers."); }
    }
}
