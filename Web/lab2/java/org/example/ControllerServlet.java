package org.example;

import java.io.*;
import java.util.ArrayList;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

@WebServlet(name = "controllerServlet", value = "/controller-servlet")
public class ControllerServlet extends HttpServlet {

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.addHeader("Access-Control-Allow-Origin", "*");
        if (req.getMethod().equalsIgnoreCase("post")){
            RequestValidator validator = new RequestValidator();
            if (validator.validate(req.getParameter("x"), req.getParameter("y"), req.getParameter("R"))){
                ServletContext context = getServletContext();
                context.setAttribute("x", validator.getX());
                context.setAttribute("y", validator.getY());
                context.setAttribute("r", validator.getR());
                resp.sendRedirect(req.getContextPath() + "/area-check-servlet");
            } else {
                try{
                    if (req.getParameter("clear").equals("true")){
                        getServletContext().setAttribute("tableValues", new ArrayList<String>());
                        resp.sendRedirect(req.getContextPath() + "/");
                    } else {
                        req.getSession().setAttribute("errorMessage", "client has send an invalid data");
                        resp.sendRedirect(req.getContextPath() + "/error.jsp");
                    }
                } catch (NullPointerException e){
                    req.getSession().setAttribute("errorMessage", "client has send an invalid data");
                    resp.sendRedirect(req.getContextPath() + "/error.jsp");
                }
            }
        } else {
            req.getSession().setAttribute("errorMessage", "Сервер принимает только POST запросы");
            resp.sendRedirect(req.getContextPath() + "/error.jsp");
        }
    }

    public void destroy() {
    }
}