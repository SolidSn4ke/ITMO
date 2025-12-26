package com.example.back.entities;

import com.example.back.data.OrganizationType;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "organization")
public class OrganizationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.PERSIST})
    @NotNull
    @Valid
    private AddressEntity officialAddress;

    @Min(0)
    private Long annualTurnover;

    @Min(0)
    private long employeesCount;

    @Min(0)
    private double rating;

    @NotNull
    @Enumerated(EnumType.STRING)
    private OrganizationType organizationType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public AddressEntity getOfficialAddress() {
        return officialAddress;
    }

    public void setOfficialAddress(AddressEntity officialAddress) {
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

    public void setOrganizationType(OrganizationType organizationType) {
        this.organizationType = organizationType;
    }

    @Override
    public String toString() {
        return "OrganizationEntity{" +
                "id=" + id +
                ", officialAddress=" + officialAddress +
                ", annualTurnover=" + annualTurnover +
                ", employeesCount=" + employeesCount +
                ", rating=" + rating +
                ", organizationType=" + organizationType +
                '}';
    }
}
