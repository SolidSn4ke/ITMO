package com.example.back.exceptions.mappers;

import com.example.back.exceptions.importing.ImportException;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception e) {
        if (e instanceof ImportException)
            throw (ImportException) e;

        return Response.status(500).entity(e.getMessage()).build();
    }
}
