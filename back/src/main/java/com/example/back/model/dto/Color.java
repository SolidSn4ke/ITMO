package com.example.back.model.dto;

public enum Color {
    RED,
    ORANGE,
    WHITE,
    BROWN;

    public static Color stringToColor(String s) {
        return switch (s.toLowerCase()) {
            case "red" -> RED;
            case "orange" -> ORANGE;
            case "white" -> WHITE;
            case "brown" -> BROWN;
            default -> null;
        };
    }
}