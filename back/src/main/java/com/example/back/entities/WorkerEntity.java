package com.example.back.entities;

import com.example.back.data.Position;
import com.example.back.data.Status;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

@Entity
@Table(name = "worker")
public class WorkerEntity {
    public WorkerEntity() {
        this.creationDate = LocalDate.now();
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    private String name;

    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    @Valid
    private CoordinatesEntity coordinates;

    @NotNull
    private LocalDate creationDate;

    @Valid
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "organizationID")
    private OrganizationEntity organization;

    @Min(0)
    private Double salary;

    @Min(0)
    private Double rating;

    @NotNull
    private LocalDate startDate;

    @Enumerated(EnumType.STRING)
    @NotNull
    private Position position;

    @Enumerated(EnumType.STRING)
    private Status status;

    @Valid
    @NotNull
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "passportID")
    private PersonEntity person;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public OrganizationEntity getOrganization() {
        return organization;
    }

    public void setOrganization(OrganizationEntity organization) {
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

    public PersonEntity getPerson() {
        return person;
    }

    public void setPerson(PersonEntity person) {
        this.person = person;
    }

    public CoordinatesEntity getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(CoordinatesEntity coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public String toString() {
        return "WorkerEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", coordinates=" + coordinates +
                ", creationDate=" + creationDate +
                ", organization=" + organization +
                ", salary=" + salary +
                ", rating=" + rating +
                ", startDate=" + startDate +
                ", position=" + position +
                ", status=" + status +
                ", person=" + person +
                '}';
    }
}
