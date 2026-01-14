package com.example.back.model.entities;

import com.example.back.model.dto.Color;
import com.example.back.model.dto.Country;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "person")
public class PersonEntity {
    @Enumerated(EnumType.STRING)
    private Color eyeColor;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Color hairColor;

    @Valid
    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH, CascadeType.PERSIST})
    private LocationEntity location;

    @Id
    @Size(min = 6)
    private String passportID;

    @Enumerated(EnumType.STRING)
    private Country nationality;

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

    public LocationEntity getLocation() {
        return location;
    }

    public void setLocation(LocationEntity location) {
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

    @Override
    public String toString() {
        return "PersonEntity{" +
                "eyeColor=" + eyeColor +
                ", hairColor=" + hairColor +
                ", location=" + location +
                ", passportID='" + passportID + '\'' +
                ", nationality=" + nationality +
                '}';
    }
}
