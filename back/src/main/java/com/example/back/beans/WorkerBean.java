package com.example.back.beans;

import com.example.back.ejb.WorkerEJB;
import com.example.back.entities.WorkerEntity;
import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import jakarta.persistence.PersistenceException;

import java.util.List;

@Named("wb")
@ApplicationScoped
public class WorkerBean {
    private List<WorkerEntity> workers;
    private String message;

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

    public boolean view() {
        try {
            workers = workerEJB.getAllWorkers();
        } catch (PersistenceException e) {
            message = e.getMessage();
            return false;
        }
        return true;
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
}
