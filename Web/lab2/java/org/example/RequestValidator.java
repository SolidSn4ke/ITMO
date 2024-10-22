package org.example;

import java.math.BigDecimal;

public class RequestValidator{
    private BigDecimal x;
    private BigDecimal y;
    private BigDecimal r;

    public boolean validate(String xValue, String yValue, String rValue) {
        try {
            x = new BigDecimal(xValue.replace(',', '.'));
            y = new BigDecimal(yValue.replace(',', '.'));
            r = new BigDecimal(rValue.replace(',', '.'));
            return !(Math.abs(x.doubleValue()) > 2) && y.doubleValue() >= -3 && y.doubleValue() <= 5 && r.doubleValue() >= 1 && r.doubleValue() <= 3;
        } catch (Exception e){
            return false;
        }
    }

    public String getX() {
        return x.toString();
    }

    public String getY() {
        return y.toString();
    }

    public String getR() {
        return r.toString();
    }
}
