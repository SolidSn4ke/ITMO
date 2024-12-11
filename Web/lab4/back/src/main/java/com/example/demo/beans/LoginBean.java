package com.example.demo.beans;

import com.example.demo.EJBbeans.UserEJB;
import com.example.demo.entities.UserEntity;

import javax.ejb.EJB;
import javax.enterprise.context.ApplicationScoped;
import javax.ws.rs.*;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.Date;

@ApplicationScoped
@Path("/user")
public class LoginBean implements Serializable {
    public LoginBean(){}

    private String login;
    private String password;

    @EJB
    private UserEJB userEJB;

    @POST
    @Path("/login/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response login(UserEntity user){
        if (userEJB.checkUserInDB(user.getLogin(), user.getPassword())){
            NewCookie cookie = new NewCookie(new Cookie("token", userEJB.getAssociatedToken(user.getLogin()), "/", "localhost"), null, 3600, new Date(System.currentTimeMillis() + 10 * 3600000),false, false);
            return Response.ok().cookie(cookie).build();
        } else {
            return Response.status(Response.Status.ACCEPTED).entity("Неверный логин или пароль").build();
        }
    }

    @POST
    @Path("/sign-in/")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response signIn(UserEntity user){
        if (!userEJB.addUserToDB(user.getLogin(), user.getPassword())){
            return Response.status(Response.Status.ACCEPTED).entity("Пользователь с таким логином уже существует").build();
        } else {
            NewCookie cookie = new NewCookie(new Cookie("token", userEJB.getAssociatedToken(user.getLogin()), "/", "localhost"), null, 3600, new Date(System.currentTimeMillis() + 10 * 3600000),false, false);
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
