package com.example.back.model.dto;

import java.time.LocalDate;

import com.example.back.model.entities.AddressEntity;
import com.example.back.model.entities.CoordinatesEntity;
import com.example.back.model.entities.LocationEntity;
import com.example.back.model.entities.OrganizationEntity;
import com.example.back.model.entities.PersonEntity;
import com.example.back.model.entities.WorkerEntity;

public class Worker {
    private Integer id; // Поле не может быть null, Значение поля должно быть больше 0, Значение этого
                        // поля должно быть уникальным, Значение этого поля должно генерироваться
                        // автоматически
    private String name; // Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; // Поле не может быть null
    private java.time.LocalDate creationDate; // Поле не может быть null, Значение этого поля должно генерироваться
                                              // автоматически
    private Organization organization; // Поле может быть null
    private Double salary; // Поле может быть null, Значение поля должно быть больше 0
    private Double rating; // Поле может быть null, Значение поля должно быть больше 0
    private java.time.LocalDate startDate; // Поле не может быть null
    private Position position; // Поле не может быть null
    private Status status; // Поле может быть null
    private Person person; // Поле не может быть null

    public WorkerEntity toEntity() {
        WorkerEntity we = new WorkerEntity();

        we.setName(this.name);
        we.setStartDate(this.startDate);
        we.setSalary(this.salary == null ? null : this.salary);
        we.setRating(this.rating == null ? null : this.rating);
        we.setPosition(this.position);
        we.setStatus(this.status == null ? null : this.status);

        CoordinatesEntity ce = new CoordinatesEntity();
        ce.setX(this.coordinates.getX());
        ce.setY(this.coordinates.getY());

        we.setCoordinates(ce);

        OrganizationEntity oe = null;
        if (this.organization != null) {
            oe = new OrganizationEntity();
            oe.setEmployeesCount(this.organization.getEmployeesCount());
            oe.setAnnualTurnover(this.organization.getAnnualTurnover() == null ? null : this.organization.getAnnualTurnover());
            oe.setOrganizationType(this.organization.getOrganizationType());
            oe.setRating(this.organization.getRating());

            AddressEntity ae = new AddressEntity();
            LocationEntity le = new LocationEntity();
            le.setX(this.organization.getOfficialAddress().getTown().getX());
            le.setY(this.organization.getOfficialAddress().getTown().getY());
            le.setZ(this.organization.getOfficialAddress().getTown().getZ());
            le.setName(this.organization.getOfficialAddress().getTown().getName());

            ae.setTown(le);
            ae.setStreet(this.organization.getOfficialAddress().getStreet());
            ae.setZipCode(this.organization.getOfficialAddress().getZipCode());

            oe.setOfficialAddress(ae);
        }
        we.setOrganization(oe);

        PersonEntity pe = new PersonEntity();
        pe.setPassportID(this.person.getPassportID());
        pe.setEyeColor(this.person.getEyeColor() == null ? null : this.person.getEyeColor());
        pe.setHairColor(this.person.getHairColor());
        pe.setNationality(this.person.getNationality() == null ? null : this.person.getNationality());

        LocationEntity le = null;
        if (this.person.getLocation() != null) {
            le = new LocationEntity();
            le.setX(this.person.getLocation().getX());
            le.setY(this.person.getLocation().getY());
            le.setZ(this.person.getLocation().getZ());
            le.setName(this.person.getLocation().getName());
        }
        pe.setLocation(le);

        we.setPerson(pe);

        return we;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }

    public Double getSalary() {
        return salary;
    }

    public void setSalary(Double salary) {
        this.salary = salary;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }
}