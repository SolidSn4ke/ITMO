package com.example.back.dto;

import com.example.back.entities.WorkerEntity;

import java.util.List;

public class ResponseDTO {
    private String message;
    private List<WorkerEntity> listOfWorkers;

    public String getMessage() {
        return message;
    }

    public ResponseDTO setMessage(String message) {
        this.message = message;
        return this;
    }

    public List<WorkerEntity> getListOfWorkers() {
        return listOfWorkers;
    }

    public ResponseDTO setListOfWorkers(List<WorkerEntity> listOfWorkers) {
        this.listOfWorkers = listOfWorkers;
        return this;
    }
}
