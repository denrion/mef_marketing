package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.PotentialStudentMail;
import com.github.denrion.mef_marketing.service.PotentialStudentMailService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("mail")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PotentialStudentMailResource {

    @Inject
    PotentialStudentMailService psMailService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAll() {
        return Response
                .ok(psMailService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getById(@PathParam("id") Long id) {
        PotentialStudentMail student = psMailService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @POST
    public Response create(@Valid PotentialStudentMail psm) {

        psMailService.save(psm);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(psm.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(psm)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid PotentialStudentMail psm) {

        PotentialStudentMail student = psMailService.update(psm, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response delete(@PathParam("id") Long id) {
        psMailService.delete(id);

        return Response
                .ok()
                .build();
    }

}
