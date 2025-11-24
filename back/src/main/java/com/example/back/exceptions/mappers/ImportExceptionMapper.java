package com.example.back.exceptions.mappers;

import com.example.back.beans.ImportHistoryBean;
import com.example.back.entities.ImportHistoryEntity;
import com.example.back.exceptions.importing.ImportException;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ImportExceptionMapper implements ExceptionMapper<ImportException> {
    @Inject
    private @Named("ihb") ImportHistoryBean ihb;

    @Override
    public jakarta.ws.rs.core.Response toResponse(ImportException e) {
        ImportHistoryEntity importRecord = new ImportHistoryEntity();
        importRecord.setSuccessful(false);
        ihb.addImportRecord(importRecord);
        return jakarta.ws.rs.core.Response.status(400).entity(e.getMessage()).build();
    }
}
