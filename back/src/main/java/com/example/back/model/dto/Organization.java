package com.example.back.model.dto;

public class Organization {
    private Address officialAddress; // Поле не может быть null
    private Long annualTurnover; // Поле может быть null, Значение поля должно быть больше 0
    private long employeesCount; // Значение поля должно быть больше 0
    private double rating; // Значение поля должно быть больше 0
    private OrganizationType organizationType; // Поле не может быть null

    public Organization(Address officialAddress, Long annualTurnover, long employeesCount, double rating,
            OrganizationType organizationType) {
        this.officialAddress = officialAddress;
        this.annualTurnover = annualTurnover;
        this.employeesCount = employeesCount;
        this.rating = rating;
        this.organizationType = organizationType;
    }

    public Address getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(Address officialAddress) {
        this.officialAddress = officialAddress;
    }

    public Long getAnnualTurnover() {
        return annualTurnover;
    }

    public void setAnnualTurnover(Long annualTurnover) {
        this.annualTurnover = annualTurnover;
    }

    public long getEmployeesCount() {
        return employeesCount;
    }

    public void setEmployeesCount(long employeesCount) {
        this.employeesCount = employeesCount;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public OrganizationType getOrganizationType() {
        return organizationType;
    }

    public void setOrganizationType(OrganizationType type) {
        this.organizationType = type;
    }
}