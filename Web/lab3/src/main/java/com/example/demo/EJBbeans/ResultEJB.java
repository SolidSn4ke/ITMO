package com.example.demo.EJBbeans;

import com.example.demo.entities.ResultEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

@Stateless
public class ResultEJB {
    public ResultEJB(){}

    public boolean addToDB(String x, String y, String r, boolean hit) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        ResultEntity newEntity = new ResultEntity();
        newEntity.setX(x);
        newEntity.setY(y);
        newEntity.setR(r);
        newEntity.setHit(hit);

        em.persist(newEntity);
        em.getTransaction().commit();

        em.close();
        emf.close();
        return true;
    }

    public List<ResultEntity> getEntities() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("select entity from ResultEntity entity");
        return query.getResultList();
    }

    public void clear() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("delete from ResultEntity entity");
        query.executeUpdate();
    }
}
