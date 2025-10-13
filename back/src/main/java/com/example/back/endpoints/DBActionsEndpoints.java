package com.example.back.endpoints;

import com.example.back.beans.WorkerBean;
import com.example.back.entities.WorkerEntity;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/actions")
public class DBActionsEndpoints {
    @Inject
    private @Named("wb") WorkerBean workerBean;


    @POST
    @Path("/add-worker")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response add(WorkerEntity worker) {
        workerBean.add(worker);
        if (workerBean.getMessage().equals("OK")) {
            return Response.ok().build();
        } else {
            return Response.accepted().entity(workerBean.getMessage()).build();
        }

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
    @Produces(MediaType.APPLICATION_JSON)
    public Response view() {
        if (workerBean.view()) {
            return Response.ok().entity(workerBean.getWorkers()).build();
        } else return Response.accepted().entity(workerBean.getMessage()).build();
    }

    @POST
    @Path("/worker-min-position")
    @Produces(MediaType.APPLICATION_JSON)
    public Response workerWithMinPosition() {
        WorkerEntity result = workerBean.getWorkerWithMinPosition();
        if (result == null) {
            return Response.accepted().entity(workerBean.getMessage()).build();
        } else return Response.ok().entity(result).build();
    }

    @POST
    @Path("/worker-max-salary")
    @Produces(MediaType.APPLICATION_JSON)
    public Response workerWithMaxSalary() {
        WorkerEntity result = workerBean.getWorkerWithMaxSalary();
        if (result == null) {
            return Response.accepted().entity(workerBean.getMessage()).build();
        } else return Response.ok().entity(result).build();
    }

    @POST
    @Path("/worker-specific-rating")
    @Produces(MediaType.APPLICATION_JSON)
    public Response workerWithMaxSalary(Integer rating) {
        if (workerBean.getWorkersWithSpecificRating(rating)) {
            return Response.ok().entity(workerBean.getWorkers()).build();
        } else return Response.ok().entity(workerBean.getMessage()).build();
    }

    @POST
    @Path("/enroll-worker/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response workerWithMaxSalary(@PathParam("id") Long id, Long organizationID) {
        if (workerBean.enrollWorker(id, organizationID)) {
            return Response.ok().build();
        } else return Response.ok().entity(workerBean.getMessage()).build();
    }
}