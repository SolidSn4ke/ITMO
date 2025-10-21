package com.example.back.entities;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;

import java.io.Serializable;

@Entity
@Table(name = "address")
public class AddressEntity implements Serializable {
    @Id
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @Valid
    private LocationEntity town;

    private String street;

    @Size(min = 9)
    private String zipCode;

    public LocationEntity getTown() {
        return town;
    }

    public void setTown(LocationEntity town) {
        this.town = town;
    }

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

    @Override
    public String toString() {
        return "AddressEntity{" +
                "town=" + town +
                ", street='" + street + '\'' +
                ", zipCode='" + zipCode + '\'' +
                '}';
    }
}
