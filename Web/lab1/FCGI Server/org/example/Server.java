package org.example;

import com.fastcgi.FCGIInterface;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Server {
    public static void main(String[] args) {
        var fcgiInterface = new FCGIInterface();
        while (fcgiInterface.FCGIaccept() >= 0) {
            if (FCGIInterface.request.params.getProperty("REQUEST_METHOD").equals("GET")){
                long start = System.currentTimeMillis();
                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss.SSS");
                RequestValidator validator = new RequestValidator();
                try{
                    System.out.printf("""
                            Content-Type: application/json; charset=utf-8
                                                
                                                
                            {"res": "%s", "x":"%d", "y":"%f", "r":"%f", "time":"%s", "work-time":"%sms"}
                            """, validator.validate(FCGIInterface.request.params.getProperty("QUERY_STRING")), validator.getX(), validator.getY(), validator.getR(), format.format(new Date()), System.currentTimeMillis() - start);
                } catch (Exception e) {
                    System.out.println("""
                            Content-Type: application/json; charset=utf-8
                            
                            
                            {"res":"error", "info":"Клиент отправил неверные данные"}
                            """);
                }
            }
        }
    }
}

