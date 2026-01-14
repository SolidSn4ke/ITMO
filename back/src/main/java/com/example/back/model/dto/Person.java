package com.example.back.model.dto;

public class Person {
    private Color eyeColor; //Поле может быть null
    private Color hairColor; //Поле не может быть null
    private Location location; //Поле может быть null
    private String passportID; //Длина строки должна быть не меньше 6, Значение этого поля должно быть уникальным, Поле не может быть null
    private Country nationality; //Поле может быть null

    public Color getEyeColor() {
        return eyeColor;
    }

    public void setEyeColor(Color eyeColor) {
        this.eyeColor = eyeColor;
    }

    public Color getHairColor() {
        return hairColor;
    }

    public void setHairColor(Color hairColor) {
        this.hairColor = hairColor;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getPassportID() {
        return passportID;
    }

    public void setPassportID(String passportID) {
        this.passportID = passportID;
    }

    public Country getNationality() {
        return nationality;
    }

    public void setNationality(Country nationality) {
        this.nationality = nationality;
    }
}