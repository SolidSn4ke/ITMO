package com.example.back.beans;

import com.example.back.ejb.WorkerEJB;
import com.example.back.entities.WorkerEntity;
import com.example.back.filters.WorkerFilter;
import com.example.back.types.ComparisonOperations;
import com.example.back.types.Pair;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Named("wb")
@ApplicationScoped
public class WorkerBean {
    private List<WorkerEntity> workers;
    private String message;
    private final HashMap<String, Pair<String, ComparisonOperations>> mapOfFilters = new HashMap<>();


    @EJB
    private WorkerEJB workerEJB;

    public boolean add(WorkerEntity worker) {
        this.message = workerEJB.addToDB(worker);
        return this.message.equals("OK");

    }

    public boolean delete(Long id) {
        this.message = workerEJB.deleteById(id);
        return this.message.equals("OK");
    }

    public boolean update(Long id, WorkerEntity worker) {
        this.message = workerEJB.updateById(id, worker);
        return this.message.equals("OK");
    }

    public boolean view(String filters) {
        try {
            String[] listOfArgs = filters.split(",");
            initFilters();
            Arrays.stream(listOfArgs).forEach(f -> {
                Pair<String[], ComparisonOperations> pair = WorkerFilter.parseFromString(f.replace(" ", ""));
                if (pair != null) {
                    mapOfFilters.put(pair.getLeft()[0], new Pair<>(pair.getLeft()[1], pair.getRight()));
                }
            });
            Pair<List<WorkerEntity>, String> result = workerEJB.getAllWorkers(mapOfFilters);
            workers = result.getLeft();
            message = result.getRight();
        } catch (PersistenceException e) {
            message = e.getMessage();
            return false;
        }
        return message.equals("");
    }

    public WorkerEntity getWorkerWithMinPosition() {
        try {
            return workerEJB.getAllWorkersSorted("position", true).get(0);
        } catch (PersistenceException | ArrayIndexOutOfBoundsException e) {
            message = e.getMessage();
            return null;
        }
    }

    public WorkerEntity getWorkerWithMaxSalary() {
        try {
            return workerEJB.getAllWorkersSorted("salary", false).get(0);
        } catch (PersistenceException | ArrayIndexOutOfBoundsException e) {
            message = e.getMessage();
            return null;
        }
    }

    public boolean getWorkersWithSpecificRating(Integer rating) {
        try {
            workers = workerEJB.getAllWorkersWithSpecificRating(rating);
            return true;
        } catch (PersistenceException e) {
            message = e.getMessage();
            return false;
        }
    }

    public boolean enrollWorker(Long id, Long organizationID) {
        this.message = workerEJB.addOrganizationToWorker(id, organizationID);
        return this.message.equals("OK");
    }

    public String getMessage() {
        return this.message;
    }

    public List<WorkerEntity> getWorkers() {
        return this.workers;
    }

    private void initFilters() {
        mapOfFilters.put("id", new Pair<>("0", ComparisonOperations.GT));
        mapOfFilters.put("name", new Pair<>("", ComparisonOperations.LIKE));
    }
}
