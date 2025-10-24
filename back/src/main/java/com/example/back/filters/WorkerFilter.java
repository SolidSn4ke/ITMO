package com.example.back.filters;

import com.example.back.data.Position;
import com.example.back.data.Status;
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
            case ANY:
                break;
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
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("name"), name));
                break;
            case LIKE:
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
                break;
        }
    }

    public void setX(Double x, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("coordinates").get("x"), x));
                break;
            case GT:
                predicates.add(cb.gt(root.get("coordinates").get("x"), x));
                break;
            case LT:
                predicates.add(cb.lt(root.get("coordinates").get("x"), x));
                break;
        }
    }

    public void setY(Integer y, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("coordinates").get("y"), y));
                break;
            case GT:
                predicates.add(cb.gt(root.get("coordinates").get("y"), y));
                break;
            case LT:
                predicates.add(cb.lt(root.get("coordinates").get("y"), y));
                break;
        }
    }

    public void setOrganizationId(Integer orgId, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("organization").get("id"), orgId));
                break;
            case GT:
                predicates.add(cb.gt(root.get("organization").get("id"), orgId));
                break;
            case LT:
                predicates.add(cb.lt(root.get("organization").get("id"), orgId));
                break;
        }
    }

    public void setCreationDate(String creationDate, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("creationDate"), creationDate));
                break;
        }
    }

    public void setSalary(Double salary, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("salary"), salary));
                break;
            case GT:
                predicates.add(cb.gt(root.get("salary"), salary));
                break;
            case LT:
                predicates.add(cb.lt(root.get("salary"), salary));
                break;
        }
    }

    public void setRating(Double rating, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("rating"), rating));
                break;
            case GT:
                predicates.add(cb.gt(root.get("rating"), rating));
                break;
            case LT:
                predicates.add(cb.lt(root.get("rating"), rating));
                break;
        }
    }

    public void setStartDate(String startDate, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("startDate"), startDate));
                break;
        }
    }

    public void setStatus(Status status, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("status"), status));
                break;
        }
    }

    public void setPosition(Position position, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("position"), position));
                break;
        }
    }

    public void setPassportId(String passportId, ComparisonOperations co) {
        switch (co) {
            case ANY:
                break;
            case EQ:
                predicates.add(cb.equal(root.get("person").get("passportID"), passportId));
                break;
            case LIKE:
                predicates.add(cb.like(root.get("person").get("passportID"), "%" + passportId + "%"));
                break;
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
