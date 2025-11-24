package com.example.back.exceptions.mappers;

import com.example.back.exceptions.importing.ImportException;

import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ImportExceptionMapper implements ExceptionMapper<ImportException> {
    @Override
    public jakarta.ws.rs.core.Response toResponse(ImportException e) {
        return jakarta.ws.rs.core.Response.status(400).entity(e.getMessage()).build();
    }
}
