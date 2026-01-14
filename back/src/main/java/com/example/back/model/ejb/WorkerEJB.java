package com.example.back.model.ejb;

import com.example.back.model.dto.ResponseDTO;
import com.example.back.model.entities.*;
import com.example.back.service.WorkerService;
import com.example.back.util.builder.FilterBuilder;
import com.example.back.util.types.ComparisonOperations;
import com.example.back.util.types.Pair;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.validation.ConstraintViolationException;

import java.util.Map;

@Stateless
public class WorkerEJB {
    @Inject
    WorkerService wb;

    @PersistenceContext(unitName = "examplePU")
    EntityManager em;

    public ResponseDTO<WorkerEntity> addToDB(WorkerEntity worker) {
        if (em.find(PersonEntity.class, worker.getPerson().getPassportID()) != null) {
            return new ResponseDTO<WorkerEntity>().setMessage("Человек с таким паспортом уже существует!");
        }

        try {
            em.merge(worker);
        } catch (ConstraintViolationException e) {
            return new ResponseDTO<WorkerEntity>().setMessage(e.getConstraintViolations().toString());
        }

        Query query = em.createQuery("select e from WorkerEntity e where e.person.passportID = :passport");
        query.setParameter("passport", worker.getPerson().getPassportID());
        return new ResponseDTO<WorkerEntity>().setMessage("OK").setList(query.getResultList());
    }

    public ResponseDTO<Object> deleteById(Long id) {
        try {
            WorkerEntity worker = em.find(WorkerEntity.class, id);
            if (worker == null) {
                return new ResponseDTO<Object>().setMessage("Работник не найден");
            }

            CoordinatesEntity coordinates = worker.getCoordinates();
            OrganizationEntity organization = worker.getOrganization();
            PersonEntity person = worker.getPerson();

            worker.setOrganization(null);
            em.remove(worker);

            if (organization != null && em
                    .createQuery(
                            "select entity from WorkerEntity entity where entity.organization.id = :organizationID")
                    .setParameter("organizationID", organization.getId()).getResultList().size() == 0) {
                AddressEntity address = organization.getOfficialAddress();
                organization.setOfficialAddress(null);
                em.remove(organization);

                if (em.createQuery(
                        "select entity from PersonEntity entity where entity.location.x = :x and entity.location.y = :y and entity.location.z = :z")
                        .setParameter("x", address.getTown().getX()).setParameter("y", address.getTown().getY())
                        .setParameter("z", address.getTown().getZ()).getResultList().size() == 0) {
                    em.remove(address);
                }
            }

            if (em.createQuery(
                    "select entity from WorkerEntity entity where entity.coordinates.x = :x and entity.coordinates.y = :y")
                    .setParameter("x", coordinates.getX()).setParameter("y", coordinates.getY()).getResultList()
                    .size() == 0) {
                em.remove(coordinates);
            }

            LocationEntity location = person.getLocation();
            person.setLocation(null);
            em.remove(person);

            if (location != null && em.createQuery(
                    "select entity from AddressEntity entity where entity.town.x = :x and entity.town.y = :y and entity.town.z = :z")
                    .setParameter("x", location.getX()).setParameter("y", location.getY())
                    .setParameter("z", location.getZ()).getResultList().size() == 0 &&
                    em.createQuery(
                            "select entity from PersonEntity entity where entity.location.x = :x and entity.location.y = :y and entity.location.z = :z")
                            .setParameter("x", location.getX()).setParameter("y", location.getY())
                            .setParameter("z", location.getZ()).getResultList().size() == 0) {
                em.remove(location);
            }

            return new ResponseDTO<Object>().setMessage("ОК");
        } catch (QueryTimeoutException e) {
            return new ResponseDTO<Object>().setMessage(e.getMessage());
        }
    }

    public ResponseDTO<Object> updateById(Long id, WorkerEntity newWorker) {
        WorkerEntity oldWorker = em.find(WorkerEntity.class, id);

        if (oldWorker != null) {
            try {
                newWorker.setId(id);
                newWorker.setCreationDate(oldWorker.getCreationDate());
                if (newWorker.getOrganization() != null && oldWorker.getOrganization() != null) {
                    newWorker.getOrganization().setId(oldWorker.getOrganization().getId());
                }
                em.merge(newWorker);
                em.flush();
                return new ResponseDTO<Object>().setMessage("OK");
            } catch (ConstraintViolationException e) {
                return new ResponseDTO<Object>().setMessage(e.getConstraintViolations().toString() + "\n" + newWorker);
            }
        } else {
            return new ResponseDTO<Object>().setMessage("Entity not found");
        }
    }

    public ResponseDTO<WorkerEntity> getAllWorkers(Map<String, Pair<String, ComparisonOperations>> filters) {
        CriteriaBuilder cb = em.getCriteriaBuilder();

        FilterBuilder fb = new FilterBuilder(cb);

        Query query;

        try {
            query = em.createQuery(fb
                    .setId(Long.parseLong(filters.get("id").getLeft()), filters.get("id").getRight())
                    .setName(filters.get("name").getLeft(), filters.get("name").getRight())
                    .setX(Double.valueOf(filters.get("x").getLeft()), filters.get("x").getRight())
                    .setY(Integer.valueOf(filters.get("y").getLeft()), filters.get("y").getRight())
                    .setOrganizationId(Integer.valueOf(filters.get("organizationId").getLeft()),
                            filters.get("organizationId").getRight())
                    .setCreationDate(filters.get("creationDate").getLeft(), filters.get("creationDate").getRight())
                    .setSalary(Double.valueOf(filters.get("salary").getLeft()), filters.get("salary").getRight())
                    .setRating(Double.valueOf(filters.get("rating").getLeft()), filters.get("rating").getRight())
                    .setStartDate(filters.get("startDate").getLeft(), filters.get("startDate").getRight())
                    .setStatus(filters.get("status").getLeft(), filters.get("status").getRight())
                    .setPosition(filters.get("position").getLeft(), filters.get("position").getRight())
                    .setPassportId(filters.get("passportId").getLeft(), filters.get("passportId").getRight())
                    .build());
        } catch (Exception e) {
            query = em.createQuery("select entity from WorkerEntity entity");
            return new ResponseDTO<WorkerEntity>().setList(query.getResultList()).setMessage(e.getMessage());
        }
        return new ResponseDTO<WorkerEntity>().setList(query.getResultList()).setMessage("");
    }

    public ResponseDTO<WorkerEntity> getAllWorkersSorted(String columnName, boolean isAscending) {
        String direction = isAscending ? "asc" : "desc";
        Query query = em
                .createQuery("select entity from WorkerEntity entity order by entity." + columnName + " " + direction);

        return new ResponseDTO<WorkerEntity>().setList(query.getResultList());
    }

    public ResponseDTO<WorkerEntity> getAllWorkersWithSpecificRating(Integer rating) {
        Query query = em.createQuery("select entity from WorkerEntity entity where entity.rating < :rating");
        query.setParameter("rating", rating);
        return new ResponseDTO<WorkerEntity>().setList(query.getResultList());
    }

    public ResponseDTO<Object> addOrganizationToWorker(Long id, Long organizationID) {
        try {
            OrganizationEntity organization = em.find(OrganizationEntity.class, organizationID);
            WorkerEntity worker = em.find(WorkerEntity.class, id);

            worker.setOrganization(organization);
            return new ResponseDTO<Object>().setMessage("ОК");
        } catch (PersistenceException e) {
            return new ResponseDTO<Object>().setMessage(e.getMessage());
        }
    }
}
