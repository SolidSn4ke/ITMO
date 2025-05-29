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

/**
 * Класс для действий к БД относительно UserEntity
 *
 * @see UserEntity
 */
@Stateless
public class UserEJB {
    public UserEJB() {
    }

    /**
     * Метод для добавления записи в БД
     * @param login Имя пользователя
     * @param password Пароль
     * @return Логическое значение, обозначающее результат добавления записи в БД (true - если успешно добавлено)
     */
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

    /**
     * Метод для проверки существования конкретного пользователя
     * @param login Имя пользователя
     * @param password Пароль
     * @return Логическое значение, обозначающее существование конкретного пользователя в БД (true - если пользователь найден)
     */
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

    /**
     * Получение пользовательского UUID по имени
     * @param login Имя пользователя
     * @return UUID пользователя в виде строки
     */
    public String getAssociatedToken(String login) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        UserEntity user = em.find(UserEntity.class, login);
        em.close();
        emf.close();

        if (user == null) return null;
        return user.getToken();
    }

    /**
     * Метод для получения имени пользователя по его UUID
     * @param token UUID в виде строки
     * @return Имя пользователя в виде строки
     */
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

    /**
     * Метод для проверки существования пользователя с конкретным именем
     * @param login Имя пользователя
     * @return Логическое значение, обозначающее существование пользователя с конкретным именем (true - если пользователь найден)
     */
    private boolean checkLoginInDB(String login) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("examplePU");
        EntityManager em = emf.createEntityManager();

        UserEntity user = em.find(UserEntity.class, login);
        em.close();
        emf.close();
        return user != null;
    }

    /**
     * Метод для хеширования строки по SHA-256
     * @param s Строка
     * @return Массив байтов
     */
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
