package com.example.back.model.ejb;

import com.example.back.model.dto.ResponseDTO;
import com.example.back.model.entities.ImportHistoryEntity;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

@Stateless
public class ImportHistoryEJB {

    @PersistenceContext(unitName = "examplePU")
    EntityManager em;

    public ResponseDTO<Object> addToDB(ImportHistoryEntity ihe) {
        em.persist(ihe);
        return new ResponseDTO<Object>().setMessage("OK");
    }

    public ResponseDTO<ImportHistoryEntity> getAllFromDB() {
        return new ResponseDTO<ImportHistoryEntity>().setMessage("OK").setList(
                em.createQuery("SELECT i FROM ImportHistoryEntity i", ImportHistoryEntity.class).getResultList());
    }

    public ResponseDTO<Object> deleteAllFromDB() {
        em.createQuery("DELETE FROM ImportHistoryEntity").executeUpdate();
        return new ResponseDTO<Object>().setMessage("OK");
    }
}
