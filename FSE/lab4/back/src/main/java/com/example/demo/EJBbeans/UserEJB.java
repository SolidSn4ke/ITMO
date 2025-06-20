package com.example.demo.EJBbeans;

import com.example.demo.entities.UserEntity;
import com.example.demo.util.Hex;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Stateless
public class UserEJB {
    public UserEJB() {
    }

    public boolean addUserToDB(String login, String password) {
        if (checkLoginInDB(login)) return false;
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        UserEntity user = new UserEntity();
        user.setLogin(login);

        byte[] hash = hashString(System.getenv("PAPER") + password + "S14D!#!af^*F");
        if (hash == null) return false;
        user.setPassword(Hex.bytesToHex(hash));
        user.setToken(UUID.randomUUID().toString());

        em.getTransaction().begin();
        em.persist(user);
        em.getTransaction().commit();

        em.close();
        emf.close();
        return true;
    }

    public boolean checkUserInDB(String login, String password) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        UserEntity user = em.find(UserEntity.class, login);
        em.close();
        emf.close();

        byte[] hash = hashString(System.getenv("PAPER") + password + "S14D!#!af^*F");
        if (hash == null) return false;
        return user != null && user.getPassword().equals(Hex.bytesToHex(hash));
    }

    public String getAssociatedToken(String login) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        UserEntity user = em.find(UserEntity.class, login);
        em.close();
        emf.close();

        if (user == null) return null;
        return user.getToken();
    }

    public String getAssociatedLogin(String token) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        Query query = em.createQuery("select u from UserEntity u where u.token=:token", UserEntity.class);
        query.setParameter("token", token);
        List list = query.getResultList();
        if (list.size() < 1) return null;

        UserEntity user = (UserEntity) list.get(0);

        em.close();
        emf.close();
        return user.getLogin();
    }

    private boolean checkLoginInDB(String login) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        UserEntity user = em.find(UserEntity.class, login);
        em.close();
        emf.close();
        return user != null;
    }


    private byte[] hashString(String s) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            return digest.digest(s.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}
