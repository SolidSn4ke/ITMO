package com.example.back.rest.endpoints;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import com.example.back.model.dto.FileDTO;
import com.example.back.model.entities.ImportHistoryEntity;
import com.example.back.model.entities.WorkerEntity;
import com.example.back.services.ImportHistoryService;
import com.example.back.services.WorkerService;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/actions")
public class DBActionsEndpoints {
    @Inject
    private @Named("wb") WorkerService workerBean;

    @Inject
    private @Named("ihb") ImportHistoryService importHistoryService;

    @POST
    @Path("/add-worker")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(WorkerEntity worker) {
        workerBean.add(worker);
        if (workerBean.getMessage().equals("OK")) {
            return Response.ok().entity(workerBean.getWorkers().get(0)).build();
        } else {
            return Response.accepted().entity(workerBean.getMessage()).build();
        }
    }

    @POST
    @Path("import-workers")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response importWorkers(FileDTO file) {
        try (InputStream fileIS = new ByteArrayInputStream(file.getContent())) {
            importHistoryService.importFile(fileIS, file.getFileName());
        } catch (IOException e) {
            return Response.accepted().entity("Parse error").build();
        }
        return Response.ok().build();
    }

    @POST
    @Path("/download-file/{fileName}")
    @Produces(MediaType.APPLICATION_OCTET_STREAM)
    public Response downloadFile(@PathParam("fileName") String fileName) {
        InputStream fileIS = importHistoryService.getFile(fileName);
        if (fileIS == null) {
            return Response.status(404).build();
        }
        return Response.ok(fileIS).build();
    }

    @POST
    @Path("/update-worker/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") Long id, WorkerEntity worker) {
        if (workerBean.update(id, worker)) {
            return Response.ok().build();
        } else {
            return Response.accepted().entity(workerBean.getMessage()).build();
        }
    }

    @POST
    @Path("/delete-worker")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response delete(Long id) {
        if (workerBean.delete(id))
            return Response.ok().build();
        else
            return Response.accepted().entity(workerBean.getMessage()).build();
    }

    @POST
    @Path("/view-workers")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.APPLICATION_JSON)
    public Response view(String filters) {
        if (workerBean.view(filters)) {
            return Response.ok().entity(workerBean.getWorkers()).build();
        } else
            return Response.accepted().entity(workerBean.getWorkers()).entity(workerBean.getMessage()).build();
    }

    @POST
    @Path("/worker-min-position")
    @Produces(MediaType.APPLICATION_JSON)
    public Response workerWithMinPosition() {
        WorkerEntity result = workerBean.getWorkerWithMinPosition();
        if (result == null) {
            return Response.accepted().entity(workerBean.getMessage()).build();
        } else
            return Response.ok().entity(result).build();
    }

    @POST
    @Path("/worker-max-salary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response workerWithMaxSalary() {
        WorkerEntity result = workerBean.getWorkerWithMaxSalary();
        if (result == null) {
            return Response.accepted().entity(workerBean.getMessage()).build();
        } else
            return Response.ok().entity(result).build();
    }

    @POST
    @Path("/worker-specific-rating")
    @Produces(MediaType.APPLICATION_JSON)
    public Response workerWithMaxSalary(Integer rating) {
        if (workerBean.getWorkersWithSpecificRating(rating)) {
            return Response.ok().entity(workerBean.getWorkers()).build();
        } else
            return Response.ok().entity(workerBean.getMessage()).build();
    }

    @POST
    @Path("/enroll-worker/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response workerWithMaxSalary(@PathParam("id") Long id, Long organizationID) {
        if (workerBean.enrollWorker(id, organizationID)) {
            return Response.ok().build();
        } else
            return Response.ok().entity(workerBean.getMessage()).build();
    }

    @POST
    @Path("/import-history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getImportHistory() {
        List<ImportHistoryEntity> records = importHistoryService.getAllImportRecords();
        return Response.ok().entity(records).build();
    }

    @POST
    @Path("/clear-import-history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearImportHistory() {
        if (importHistoryService.deleteAllImportRecords()) {
            return Response.ok().build();
        } else {
            return Response.accepted().entity("Failed to clear import history").build();
        }
    }
}