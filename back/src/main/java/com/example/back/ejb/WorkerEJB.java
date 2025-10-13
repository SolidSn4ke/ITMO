package com.example.back.ejb;

import com.example.back.entities.*;
import jakarta.ejb.Stateless;
import jakarta.persistence.*;
import jakarta.validation.ConstraintViolationException;

import java.util.List;

@Stateless
public class WorkerEJB {
    public String addToDB(WorkerEntity worker) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        if (em.find(PersonEntity.class, worker.getPerson().getPassportID()) != null) {
            return "Человек с таким паспортом уже существует!";
        }

        try {
            em.merge(worker);
            em.close();
            emf.close();
        } catch (ConstraintViolationException e) {
            return e.getConstraintViolations().toString();
        }

        return "OK";
    }

    public String deleteById(Long id) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        try {
            WorkerEntity worker = em.find(WorkerEntity.class, id);

            CoordinatesEntity coordinates = worker.getCoordinates();
            OrganizationEntity organization = worker.getOrganization();
            PersonEntity person = worker.getPerson();

            worker.setOrganization(null);
            em.remove(worker);

            if (organization != null && em.createQuery("select entity from WorkerEntity entity where entity.organization.id = :organizationID").setParameter("organizationID", organization.getId()).getResultList().size() == 0) {
                AddressEntity address = organization.getOfficialAddress();
                em.remove(organization);

                if (em.createQuery("select entity from PersonEntity entity where entity.location.x = :x and entity.location.y = :y and entity.location.z = :z").setParameter("x", address.getTown().getX()).setParameter("y", address.getTown().getY()).setParameter("z", address.getTown().getZ()).getResultList().size() == 0) {
                    em.remove(address);
                }
            }

            if (em.createQuery("select entity from WorkerEntity entity where entity.coordinates.x = :x and entity.coordinates.y = :y").setParameter("x", coordinates.getX()).setParameter("y", coordinates.getY()).getResultList().size() == 0) {
                em.remove(coordinates);
            }

            LocationEntity location = person.getLocation();
            person.setLocation(null);
            em.remove(person);

            if (location != null && em.createQuery("select entity from AddressEntity entity where entity.town.x = :x and entity.town.y = :y and entity.town.z = :z").setParameter("x", location.getX()).setParameter("y", location.getY()).setParameter("z", location.getZ()).getResultList().size() == 0 &&
                    em.createQuery("select entity from PersonEntity entity where entity.location.x = :x and entity.location.y = :y and entity.location.z = :z").setParameter("x", location.getX()).setParameter("y", location.getY()).setParameter("z", location.getZ()).getResultList().size() == 0) {
                em.remove(location);
            }

            return "OK";
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
