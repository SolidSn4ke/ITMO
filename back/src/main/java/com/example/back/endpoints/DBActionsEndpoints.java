package com.example.back.endpoints;

import java.util.List;

import com.example.back.beans.ImportHistoryBean;
import com.example.back.beans.WorkerBean;
import com.example.back.data.Worker;
import com.example.back.entities.ImportHistoryEntity;
import com.example.back.entities.WorkerEntity;
import com.example.back.exceptions.importing.ImportException;

import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/actions")
public class DBActionsEndpoints {
    @Inject
    private @Named("wb") WorkerBean workerBean;

    @Inject
    private @Named("ihb") ImportHistoryBean importHistoryBean;

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
    @Transactional
    public Response importWorkers(List<Worker> workers) {
        ImportHistoryEntity importRecord = new ImportHistoryEntity();
        workers.stream().map(Worker::toEntity).forEach(e -> {
            workerBean.add(e);
            if (!workerBean.getMessage().equals("OK")) {
                throw new ImportException(
                        String.format("Failed to import file: %s\nGot: %s", workerBean.getMessage(), e.toString()));
            }
        });
        importRecord.setSuccessful(true);
        importRecord.setNumOfEntitiesImported((long) workers.size());
        importHistoryBean.addImportRecord(importRecord);
        return Response.ok().build();
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
        List<ImportHistoryEntity> records = importHistoryBean.getAllImportRecords();
        return Response.ok().entity(records).build();
    }

    @POST
    @Path("/clear-import-history")
    @Produces(MediaType.APPLICATION_JSON)
    public Response clearImportHistory() {
        if (importHistoryBean.deleteAllImportRecords()) {
            return Response.ok().build();
        } else {
            return Response.accepted().entity("Failed to clear import history").build();
        }
    }
}