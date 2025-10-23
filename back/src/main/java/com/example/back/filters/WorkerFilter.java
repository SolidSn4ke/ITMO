package com.example.back.filters;

import com.example.back.entities.WorkerEntity;
import com.example.back.types.ComparisonOperations;
import com.example.back.types.Pair;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;

public class WorkerFilter {
    public WorkerFilter(CriteriaBuilder cb) {
        this.cb = cb;
        this.query = cb.createQuery(WorkerEntity.class);
        this.root = query.from(WorkerEntity.class);
        this.query.select(root);
    }

    private final CriteriaBuilder cb;
    private final Root<WorkerEntity> root;
    private CriteriaQuery<WorkerEntity> query;
    private final List<Predicate> predicates = new ArrayList<>();

    public void setId(Long id, ComparisonOperations co) {
        switch (co) {
            case EQ:
                predicates.add(cb.equal(root.get("id"), id));
                break;
            case GT:
                predicates.add(cb.gt(root.get("id"), id));
                break;
            case LT:
                predicates.add(cb.lt(root.get("id"), id));
                break;
        }
    }

    public void setName(String name, ComparisonOperations co) {
        switch (co) {
            case EQ:
                predicates.add(cb.equal(root.get("name"), name));
                break;
            case LIKE:
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
        }
    }

    public static Pair<String[], ComparisonOperations> parseFromString(String filter) {
        if (filter.contains(">")) {
            return new Pair<>(filter.split(">"), ComparisonOperations.GT);
        } else if (filter.contains("<")) {
            return new Pair<>(filter.split("<"), ComparisonOperations.LT);
        } else if (filter.contains("="))
            return new Pair<>(filter.split("="), ComparisonOperations.EQ);
        else if (filter.contains("~"))
            return new Pair<>(filter.split("~"), ComparisonOperations.LIKE);
        return null;
    }

    public CriteriaQuery<WorkerEntity> getQuery() {
        if (predicates.size() != 0)
            return this.query.where(cb.and(predicates.toArray(new Predicate[0])));
        else return this.query;
    }
}
