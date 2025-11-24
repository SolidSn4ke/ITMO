package com.example.back.data;

public class Address {
    private String street; //Поле может быть null
    private String zipCode; //Длина строки должна быть не меньше 9, Поле может быть null
    private Location town; //Поле не может быть null

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getZipCode() {
        return zipCode;
    }

    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    public Location getTown() {
        return town;
    }

    public void setTown(Location town) {
        this.town = town;
    }
}