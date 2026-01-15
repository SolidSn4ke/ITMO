package com.example.back.services;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.back.config.LogCacheStats;
import com.example.back.model.dto.ResponseDTO;
import com.example.back.model.dto.Worker;
import com.example.back.model.ejb.ImportHistoryEJB;
import com.example.back.model.entities.ImportHistoryEntity;
import com.example.back.util.parcers.CSVToWorkerParcer;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

@ApplicationScoped
@Named("ihb")
@LogCacheStats
public class ImportHistoryService {

    @EJB
    ImportHistoryEJB ihEJB;

    @Inject
    MinIOClientService minioService;

    public boolean importFile(InputStream fileIS, String fileName) {
        byte[] fileBytes = null;
        try {
            fileBytes = fileIS.readAllBytes();
        } catch (IOException e) {
            e.printStackTrace();
        }
        List<Worker> workers = new CSVToWorkerParcer().parse(new ByteArrayInputStream(fileBytes));
        ResponseDTO<Object> response = ihEJB.importFile(new ByteArrayInputStream(fileBytes), fileName, workers);
        return response.getMessage().equals("OK");
    }

    public InputStream getFile(String fileName) {
        return minioService.getFile(fileName);
    }

    public boolean addImportRecord(ImportHistoryEntity ihe) {
        return ihEJB.addToDB(ihe).getMessage().equals("OK");
    }

    public List<ImportHistoryEntity> getAllImportRecords() {
        return ihEJB.getAllFromDB().getListOfEntities();
    }

    public boolean deleteAllImportRecords() {
        return ihEJB.deleteAllFromDB().getMessage().equals("OK");
    }
}
