package com.example.demo.beans;

import com.example.demo.EJBbeans.UserEJB;
import com.example.demo.entities.UserEntity;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

/**
 * Бин для работы с пользователями
 */
@ApplicationScoped
@Path("/user")
public class LoginBean implements Serializable {
    public LoginBean() {
    }

    private String login;
    private String password;

    private final ResourceBundle rb = ResourceBundle.getBundle("server_response");

    @EJB
    private UserEJB userEJB;

    /**
     * Метод для авторизации пользователя
     *
     * @param user Пользователь
     * @return Возвращает куку, если авторизация прошла успешно
     */
    @POST
    @Path("/login/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserEntity user) {
        if (userEJB.checkUserInDB(user.getLogin(), user.getPassword())) {
            NewCookie cookie = new NewCookie(new Cookie("token", userEJB.getAssociatedToken(user.getLogin()), "/", "localhost"), null, 3600, new Date(System.currentTimeMillis() + 10 * 3600000), false, false);
            return Response.ok().cookie(cookie).build();
        } else {
            return Response.status(Response.Status.ACCEPTED).entity(rb.getString("incorrect_login_or_password")).build();
        }
    }

    /**
     * Метод для регистрации пользователя
     *
     * @param user Пользователь
     * @return Возвращает куку, если регистрация прошла успешно
     */
    @POST
    @Path("/sign-in/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signIn(UserEntity user) {
        if (!userEJB.addUserToDB(user.getLogin(), user.getPassword())) {
            return Response.status(Response.Status.ACCEPTED).entity(rb.getString("user_already_registered")).build();
        } else {
            NewCookie cookie = new NewCookie(new Cookie("token", userEJB.getAssociatedToken(user.getLogin()), "/", "localhost"), null, 3600, new Date(System.currentTimeMillis() + 10 * 3600000), false, false);
            return Response.status(Response.Status.CREATED).cookie(cookie).build();
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
