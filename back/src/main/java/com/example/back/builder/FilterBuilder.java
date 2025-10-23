package com.example.back.builder;

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

    @Override
    public CriteriaQuery<WorkerEntity> build() {
        return filter.getQuery();
    }
}
