package com.example.back.ejb;

import com.example.back.entities.OrganizationEntity;
import com.example.back.entities.WorkerEntity;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

@Stateless
public class WorkerEJB {
    public String addToDB(WorkerEntity worker) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        try {
            em.merge(worker);
            em.close();
            emf.close();
        } catch (ConstraintViolationException e) {
            return e.getConstraintViolations().toString();
        }

        return "OK";
    }

    public String deleteById(Integer id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        try {
            Query query = em.createQuery("delete from WorkerEntity entity where entity.id=:id");
            query.setParameter("id", id);
            if (query.executeUpdate() > 0)
                return "OK";
            else return "FAIL";

        } catch (QueryTimeoutException e) {
            return e.getMessage();
        } finally {
            em.close();
            emf.close();
        }
    }


    public String updateById(Long id, WorkerEntity newWorker) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        WorkerEntity oldWorker = em.find(WorkerEntity.class, id);


        if (oldWorker != null) {
            try {
                newWorker.setId(id);
                newWorker.setCreationDate(oldWorker.getCreationDate());
                if (newWorker.getOrganization() != null) {
                    newWorker.getOrganization().setId(oldWorker.getOrganization().getId());
                }
                em.merge(newWorker);
                em.flush();
                return "OK";
            } catch (ConstraintViolationException e) {
                return e.getConstraintViolations().toString() + "\n" + newWorker;
            }
        } else {
            return "Entity not found";
        }
    }

    public List<WorkerEntity> getAllWorkers() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("select entity from WorkerEntity entity");
        return query.getResultList();
    }

    public List<WorkerEntity> getAllWorkersSorted(String columnName, boolean isAscending) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        String direction = isAscending ? "asc" : "desc";
        Query query = em.createQuery("select entity from WorkerEntity entity order by entity." + columnName + " " + direction);

        return query.getResultList();
    }

    public List<WorkerEntity> getAllWorkersWithSpecificRating(Integer rating) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("select entity from WorkerEntity entity where entity.rating < :rating");
        query.setParameter("rating", rating);
        return query.getResultList();
    }

    public String addOrganizationToWorker(Long id, Long organizationID) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        try {
            OrganizationEntity organization = em.find(OrganizationEntity.class, organizationID);
            WorkerEntity worker = em.find(WorkerEntity.class, id);

            worker.setOrganization(organization);
            return "OK";
        } catch (PersistenceException e) {
            return e.getMessage();
        }
    }
}
