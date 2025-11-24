package com.example.back.beans;

import java.util.List;

import com.example.back.ejb.ImportHistoryEJB;
import com.example.back.entities.ImportHistoryEntity;

import jakarta.ejb.EJB;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;

@ApplicationScoped
@Named("ihb")
public class ImportHistoryBean {
    
    @EJB
    ImportHistoryEJB ihEJB;

    public boolean addImportRecord(ImportHistoryEntity ihe){
        return ihEJB.addToDB(ihe).getMessage().equals("OK");
    }

    public List<ImportHistoryEntity> getAllImportRecords(){
        return ihEJB.getAllFromDB().getListOfEntities();
    }

    public boolean deleteAllImportRecords(){
        return ihEJB.deleteAllFromDB().getMessage().equals("OK");
    }
}
