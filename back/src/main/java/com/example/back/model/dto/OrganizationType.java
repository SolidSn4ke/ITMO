package com.example.back.model.dto;

public enum OrganizationType {
    COMMERCIAL,
    GOVERNMENT,
    TRUST,
    PRIVATE_LIMITED_COMPANY,
    OPEN_JOINT_STOCK_COMPANY;

    public static OrganizationType stringToOrganizationType(String s) {
        return switch (s.toLowerCase()) {
            case "commercial" -> COMMERCIAL;
            case "government" -> GOVERNMENT;
            case "trust" -> TRUST;
            case "private_limited_company" -> PRIVATE_LIMITED_COMPANY;
            case "open_joint_stock_company" -> OPEN_JOINT_STOCK_COMPANY;
            default -> null;
        };
    }
}