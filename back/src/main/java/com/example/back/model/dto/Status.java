package com.example.back.model.dto;

public enum Status {
    FIRED,
    RECOMMENDED_FOR_PROMOTION,
    REGULAR,
    PROBATION;

    public static Status stringToStatus(String s) {
        return switch (s.toLowerCase()) {
            case "fired" -> Status.FIRED;
            case "recommended_for_promotion" -> Status.RECOMMENDED_FOR_PROMOTION;
            case "regular" -> Status.REGULAR;
            case "probation" -> Status.PROBATION;
            default -> null;
        };
    }
}