package org.example;

import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet(name = "areaCheckServlet", value = "/area-check-servlet")
public class AreaCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ServletContext context = getServletContext();
        String x = (String) context.getAttribute("x");
        String y = (String)  context.getAttribute("y");
        String r = (String) context.getAttribute("r");
        context.setAttribute("res", String.valueOf(checkAreaHit(Double.valueOf(x), Double.valueOf(y), Double.valueOf(r))));
        resp.sendRedirect(req.getContextPath() + "/res.jsp");
    }

    private boolean checkAreaHit(Double x, Double y, Double r){
        if (x >= 0){
            if (y >= 0){
                return x <= r && y <= r/2;
            } else {
                return false;
            }
        } else {
            if (y >= 0){
                return y <= 0.5 * x + r/2;
            } else {
                return Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r, 2)/4;
            }
        }
    }
}
