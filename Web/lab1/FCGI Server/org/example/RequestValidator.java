package org.example;

public class RequestValidator {
    private Integer x;
    private Double y;
    private Double r;
    public boolean validate(String string){
        String[] params = string.split("&");
        x = Integer.valueOf(params[0].substring(2));
        y = Double.valueOf(params[1].substring(2).replace(',','.'));
        r = Double.valueOf(params[2].substring(2).replace(',','.'));
        return areaHitCheck();
    }

    private boolean areaHitCheck(){
        if (x >= 0){
            if (y >= 0){
                return y <= r/2 + x * -0.5;
            } else {
                return y >= -r && x <= r;
            }
        } else {
            if (y >= 0){
                return x * x + y * y <= r * r / 4;
            } else return false;
        }
    }

    public Integer getX() {
        return x;
    }

    public Double getY() {
        return y;
    }

    public Double getR() {
        return r;
    }
}
