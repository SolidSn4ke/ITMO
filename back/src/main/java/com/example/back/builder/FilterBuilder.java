package com.example.back.builder;

import com.example.back.data.Position;
import com.example.back.data.Status;
import com.example.back.entities.WorkerEntity;
import com.example.back.filters.WorkerFilter;
import com.example.back.types.ComparisonOperations;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;

public class FilterBuilder extends Builder {
    public FilterBuilder(CriteriaBuilder criteriaBuilder) {
        this.filter = new WorkerFilter(criteriaBuilder);
    }

    private final WorkerFilter filter;

    public FilterBuilder setId(Long id, ComparisonOperations co) {
        if (id == null) return this;
        filter.setId(id, co);
        return this;
    }

    public FilterBuilder setName(String name, ComparisonOperations co) {
        if (name == null) return this;
        filter.setName(name, co);
        return this;
    }

    public FilterBuilder setX(Double x, ComparisonOperations co) {
        if (x == null) return this;
        filter.setX(x, co);
        return this;
    }

    public FilterBuilder setY(Integer y, ComparisonOperations co) {
        if (y == null) return this;
        filter.setY(y, co);
        return this;
    }

    public FilterBuilder setOrganizationId(Integer orgId, ComparisonOperations co) {
        if (orgId == null) return this;
        filter.setOrganizationId(orgId, co);
        return this;
    }

    public FilterBuilder setCreationDate(String creationDate, ComparisonOperations co) {
        if (creationDate == null) return this;
        filter.setCreationDate(creationDate, co);
        return this;
    }

    public FilterBuilder setSalary(Double salary, ComparisonOperations co) {
        if (salary == null) return this;
        filter.setSalary(salary, co);
        return this;
    }

    public FilterBuilder setRating(Double rating, ComparisonOperations co) {
        if (rating == null) return this;
        filter.setRating(rating, co);
        return this;
    }

    public FilterBuilder setStartDate(String startDate, ComparisonOperations co) {
        if (startDate == null) return this;
        filter.setStartDate(startDate, co);
        return this;
    }

    public FilterBuilder setStatus(String status, ComparisonOperations co) {
        if (status == null) return this;
        filter.setStatus(Status.valueOf(status.toUpperCase()), co);
        return this;
    }

    public FilterBuilder setPosition(String position, ComparisonOperations co) {
        if (position == null) return this;
        filter.setPosition(Position.valueOf(position.toUpperCase()), co);
        return this;
    }

    public FilterBuilder setPassportId(String passportId, ComparisonOperations co) {
        if (passportId == null) return this;
        filter.setPassportId(passportId, co);
        return this;
    }

    @Override
    public CriteriaQuery<WorkerEntity> build() {
        return filter.getQuery();
    }
}
