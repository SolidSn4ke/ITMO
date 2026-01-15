package com.example.back.model.ejb;

import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import com.example.back.model.dto.ResponseDTO;
import com.example.back.model.dto.Worker;
import com.example.back.model.entities.ImportHistoryEntity;
import com.example.back.services.MinIOClientService;

import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

@Stateless
public class ImportHistoryEJB {

    // @PersistenceContext(unitName = "examplePU")
    // EntityManager em;

    @Inject
    MinIOClientService minioService;

    EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");

    private EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public ResponseDTO<Object> importFile(InputStream fileIS, String fileName, List<Worker> workers) {
        EntityManager em = getEntityManager();
        ImportHistoryEntity ihe = new ImportHistoryEntity();
        String objectName = UUID.randomUUID() + "-" + fileName;

        em.getTransaction().begin();

        try {
            // PREPARE

            minioService.uploadFile(fileIS, objectName);

            for (Worker worker : workers) {
                em.persist(worker.toEntity());
            }

            ihe.setSuccessful(true);
            ihe.setNumOfEntitiesImported((long) workers.size());
            ihe.setFilePath(objectName);
            em.persist(ihe);

            // COMMIT

            em.getTransaction().commit();

            return new ResponseDTO<Object>().setMessage("OK");

        } catch (Exception e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }

            try {
                minioService.deleteFile(objectName);
            } catch (Exception ignored) {
            }

            try {
                em.getTransaction().begin();
                ihe.setSuccessful(false);
                ihe.setNumOfEntitiesImported(0L);
                ihe.setFilePath(null);
                em.persist(ihe);
                em.getTransaction().commit();
            } catch (Exception ignored) {
            }

            return new ResponseDTO<Object>().setMessage("Error during import: " + e.getMessage());
        }
    }

    public ResponseDTO<Object> addToDB(ImportHistoryEntity ihe) {
        EntityManager em = getEntityManager();
        em.persist(ihe);
        return new ResponseDTO<Object>().setMessage("OK");
    }

    public ResponseDTO<ImportHistoryEntity> getAllFromDB() {
        EntityManager em = getEntityManager();
        return new ResponseDTO<ImportHistoryEntity>().setMessage("OK").setList(
                em.createQuery("SELECT i FROM ImportHistoryEntity i", ImportHistoryEntity.class).getResultList());
    }

    public ResponseDTO<Object> deleteAllFromDB() {
        EntityManager em = getEntityManager();
        em.createQuery("DELETE FROM ImportHistoryEntity").executeUpdate();
        return new ResponseDTO<Object>().setMessage("OK");
    }
}
