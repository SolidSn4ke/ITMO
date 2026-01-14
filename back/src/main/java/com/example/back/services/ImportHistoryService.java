package com.example.back.services;

import java.util.List;

import com.example.back.config.LogCacheStats;
import com.example.back.model.ejb.ImportHistoryEJB;
import com.example.back.model.entities.ImportHistoryEntity;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("ihb")
@LogCacheStats
public class ImportHistoryService {

    @EJB
    ImportHistoryEJB ihEJB;

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
