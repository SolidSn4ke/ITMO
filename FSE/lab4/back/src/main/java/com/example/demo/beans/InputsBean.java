package com.example.demo.beans;

import com.example.demo.EJBbeans.ResultEJB;
import com.example.demo.EJBbeans.UserEJB;
import com.example.demo.entities.ResultEntity;
import com.example.demo.mbeans.OnTargetPercent;
import com.example.demo.mbeans.DotsCounter;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.io.Serializable;
import java.util.List;

@Path("/result")
public class InputsBean implements Serializable {
    public InputsBean() {
    }

    private String x;
    private String y;
    private String r;

    private List<ResultEntity> allResults;

    @EJB
    private ResultEJB resultEJB;

    @EJB
    private UserEJB userEJB;

    @Inject
    private DotsCounter dotsCounter;

    @Inject
    OnTargetPercent onTargetPercent;


    private boolean validate() {
        return x.matches("^(?:-?[0-2][.,]\\d+|-[34][.,]\\d+|(?:-[1-5]|[0-3])([.,]0+)?)$")
                && y.matches("^(?:-?[0-4][.,]\\d+|(?:-[1-5]|[0-5])([.,]0+)?)$")
                && r.matches("^(?:-[1-5]|[0-3])$")
                && -5 <= Double.parseDouble(x) && Double.parseDouble(x) <= 3
                && Math.abs(Double.parseDouble(y)) <= 5
                && -5 <= Double.parseDouble(r) && Double.parseDouble(r) <= 3;
    }

    private boolean checkAreaHit(double x, double y, double r) {
        if (r < 0) {
            r = -r;
            x = -x;
            y = -y;
        }
        if (x >= 0) {
            if (y >= 0) {
                return y <= -x + r;
            } else {
                return x <= r / 2 && Math.abs(y) < r;
            }
        } else {
            if (y >= 0) {
                return Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r / 2, 2);
            } else return false;
        }
    }

    @POST
    @Path("/add-to-db")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addToDB(@CookieParam("token") String token, ResultEntity result) {
        if (token == null || token.equals(""))
            return Response.status(Response.Status.ACCEPTED).entity("Вы не авторизованы").build();
        String login = userEJB.getAssociatedLogin(token);
        if (login == null)
            return Response.status(Response.Status.ACCEPTED).entity("Не удалось определить имя пользователя").build();
        setX(result.getX());
        setY(result.getY());
        setR(result.getR());
        if (validate()) {
            resultEJB.addToDB(x, y, r, checkAreaHit(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(r)), login);
            dotsCounter.registerNewDot(checkAreaHit(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(r)));
            onTargetPercent.updatePercentage(checkAreaHit(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(r)));
            return Response.status(Response.Status.CREATED).build();
        } else return Response.status(Response.Status.ACCEPTED).entity("Серверу были переданы неверные данные").build();
    }

    @POST
    @Path("/view-all")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getEntities() {
        return Response.status(Response.Status.ACCEPTED).entity(resultEJB.getEntities()).build();
    }

    @POST
    @Path("/clear")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response clear(@CookieParam("token") String token) {
        if (token == null || token.equals(""))
            return Response.status(Response.Status.ACCEPTED).entity("Вы не авторизованы").build();
        String login = userEJB.getAssociatedLogin(token);
        if (login == null)
            return Response.status(Response.Status.ACCEPTED).entity("Не удалось определить имя пользователя").build();
        resultEJB.clear(login);
        return Response.ok().build();
    }

    public String getX() {
        return x;
    }

    public void setX(String x) {
        this.x = x.replace(',', '.');
    }

    public String getY() {
        return y;
    }

    public void setY(String y) {
        this.y = y.replace(',', '.');
    }

    public String getR() {
        return r;
    }

    public void setR(String r) {
        this.r = r.replace(',', '.');
    }

    public List<ResultEntity> getAllResults() {
        return allResults;
    }

    public void setAllResults(List<ResultEntity> allResults) {
        this.allResults = allResults;
    }
}
