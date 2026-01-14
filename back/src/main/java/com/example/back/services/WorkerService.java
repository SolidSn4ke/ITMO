package com.example.back.services;

import com.example.back.config.LogCacheStats;
import com.example.back.model.dto.ResponseDTO;
import com.example.back.model.ejb.WorkerEJB;
import com.example.back.model.entities.WorkerEntity;
import com.example.back.util.filter.WorkerFilter;
import com.example.back.util.types.ComparisonOperations;
import com.example.back.util.types.Pair;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Named("wb")
@ApplicationScoped
@LogCacheStats
public class WorkerService {
    private List<WorkerEntity> workers;
    private String message;
    private final HashMap<String, Pair<String, ComparisonOperations>> mapOfFilters = new HashMap<>();

    @EJB
    private WorkerEJB workerEJB;

    public boolean add(WorkerEntity worker) {
        ResponseDTO<WorkerEntity> response = workerEJB.addToDB(worker);
        this.message = response.getMessage();
        this.workers = response.getListOfEntities();
        return this.message.equals("OK");
    }

    public boolean delete(Long id) {
        this.message = workerEJB.deleteById(id).getMessage();
        return this.message.equals("OK");
    }

    public boolean update(Long id, WorkerEntity worker) {
        this.message = workerEJB.updateById(id, worker).getMessage();
        return this.message.equals("OK");
    }

    public boolean view(String filters) {
        try {
            String[] listOfArgs = filters.split(",");
            initFilters();
            Arrays.stream(listOfArgs).forEach(f -> {
                Pair<String[], ComparisonOperations> pair = WorkerFilter.parseFromString(f.replace(" ", ""));
                if (pair != null && pair.getLeft().length > 1) {
                    mapOfFilters.put(pair.getLeft()[0], new Pair<>(pair.getLeft()[1], pair.getRight()));
                }
            });
            ResponseDTO<WorkerEntity> responseDTO = workerEJB.getAllWorkers(mapOfFilters);
            workers = responseDTO.getListOfEntities();
            message = responseDTO.getMessage();
        } catch (PersistenceException e) {
            message = e.getMessage();
            return false;
        }
        return message.equals("");
    }

    public WorkerEntity getWorkerWithMinPosition() {
        try {
            return workerEJB.getAllWorkersSorted("position", true).getListOfEntities().get(0);
        } catch (PersistenceException | ArrayIndexOutOfBoundsException e) {
            message = e.getMessage();
            return null;
        }
    }

    public WorkerEntity getWorkerWithMaxSalary() {
        try {
            return workerEJB.getAllWorkersSorted("salary", false).getListOfEntities().get(0);
        } catch (PersistenceException | ArrayIndexOutOfBoundsException e) {
            message = e.getMessage();
            return null;
        }
    }

    public boolean getWorkersWithSpecificRating(Integer rating) {
        try {
            workers = workerEJB.getAllWorkersWithSpecificRating(rating).getListOfEntities();
            return true;
        } catch (PersistenceException e) {
            message = e.getMessage();
            return false;
        }
    }

    public boolean enrollWorker(Long id, Long organizationID) {
        this.message = workerEJB.addOrganizationToWorker(id, organizationID).getMessage();
        return this.message.equals("OK");
    }

    public String getMessage() {
        return this.message;
    }

    public List<WorkerEntity> getWorkers() {
        return this.workers;
    }

    private void initFilters() {
        mapOfFilters.put("id", new Pair<>("0", ComparisonOperations.ANY));
        mapOfFilters.put("name", new Pair<>("", ComparisonOperations.ANY));
        mapOfFilters.put("x", new Pair<>("0", ComparisonOperations.ANY));
        mapOfFilters.put("y", new Pair<>("0", ComparisonOperations.ANY));
        mapOfFilters.put("organizationId", new Pair<>("0", ComparisonOperations.ANY));
        mapOfFilters.put("creationDate", new Pair<>("", ComparisonOperations.ANY));
        mapOfFilters.put("salary", new Pair<>("0", ComparisonOperations.ANY));
        mapOfFilters.put("rating", new Pair<>("0", ComparisonOperations.ANY));
        mapOfFilters.put("startDate", new Pair<>("", ComparisonOperations.ANY));
        mapOfFilters.put("position", new Pair<>("COOK", ComparisonOperations.ANY));
        mapOfFilters.put("status", new Pair<>("REGULAR", ComparisonOperations.ANY));
        mapOfFilters.put("passportId", new Pair<>("", ComparisonOperations.ANY));
    }
}
