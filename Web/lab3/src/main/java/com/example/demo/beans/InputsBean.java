package com.example.demo.beans;

import com.example.demo.EJBbeans.ResultEJB;
import com.example.demo.entities.ResultEntity;

import javax.ejb.EJB;
import java.io.Serializable;
import java.util.List;

public class InputsBean implements Serializable {
    public InputsBean(){}

    private String x;
    private String y;
    private String r;

    private List<ResultEntity> allResults;

    @EJB
    private ResultEJB resultEJB;

    private boolean validate() {
        return  y.matches("^(?:-?[0-2][.,]\\d+|-?[1-3]([.,]0+)?|0([.,]\\d+)?)$") && Math.abs(Double.parseDouble(x)) <= 4 && Math.abs(Double.parseDouble(y)) <= 3 && Double.parseDouble(r) >= 1 && Double.parseDouble(r) <= 3;
    }

    private boolean checkAreaHit(double x, double y, double r) {
        if (x >= 0) {
            if (y >= 0) {
                return Math.pow(x, 2) + Math.pow(y, 2) <= Math.pow(r, 2);
            } else return false;
        } else {
            if (y >= 0) {
                return Math.abs(x) <= r && y <= r / 2;
            } else {
                return y >= -1 * x - r;
            }
        }
    }

    public boolean addToDB() {
        if (validate()) {
            return resultEJB.addToDB(x, y, r, checkAreaHit(Double.parseDouble(x), Double.parseDouble(y), Double.parseDouble(r)));
        } else return false;
    }

    public void getEntities() {
        allResults = resultEJB.getEntities();

    }

    public void clear() {
        resultEJB.clear();
        getEntities();
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
