package com.example.demo.EJBbeans;

import com.example.demo.entities.ResultEntity;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.util.List;

/**
 * Класс для действий к БД относительно ResultEntity
 * @see ResultEntity
 */
@Stateless
public class ResultEJB {
    public ResultEJB(){}

    /**
     * Метод для добавления записи в БД
     * @param x Координата x
     * @param y Координата y
     * @param r Радиус изображения
     * @param hit Логическая переменная, обозначающая попадание в область
     * @param author Имя пользователя, добавившего данную запись
     * @return Логическое значение, обозначающее результат добавления записи в БД (true - если успешно добавлено)
     */
    public boolean addToDB(String x, String y, String r, boolean hit, String author) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        ResultEntity newEntity = new ResultEntity();
        newEntity.setX(x);
        newEntity.setY(y);
        newEntity.setR(r);
        newEntity.setHit(hit);
        newEntity.setAuthor(author);

        em.persist(newEntity);
        em.getTransaction().commit();

        em.close();
        emf.close();
        return true;
    }

    /**
     * Метод для получения всех записей точек, хранящихся в БД
     * @return Список ResultEntity
     * @see ResultEntity
     */
    public List<ResultEntity> getEntities() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("select entity from ResultEntity entity");
        return query.getResultList();
    }

    /**
     * Удаление всех записей точках в БД для конкретного пользователя
     * @param author Имя пользователя
     */
    public void clear(String author) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("delete from ResultEntity entity where entity.author=:author");
        query.setParameter("author", author);
        query.executeUpdate();
    }
}
