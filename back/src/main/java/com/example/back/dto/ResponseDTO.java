package com.example.back.dto;

import java.util.List;

public class ResponseDTO<T> {
    private String message;
    private List<T> listOfEntities;

    public String getMessage() {
        return message;
    }

    public ResponseDTO<T> setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<T> getListOfEntities() {
        return listOfEntities;
    }

    public ResponseDTO<T> setList(List<T> list) {
        this.listOfEntities = list;
        return this;
    }
}
