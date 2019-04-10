package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.PotentialStudentPhone;
import com.github.denrion.mef_marketing.service.PotentialStudentPhoneService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("phone")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PotentialStudentPhoneResource {

    @Inject
    PotentialStudentPhoneService psPhoneService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAll() {
        return Response
                .ok(psPhoneService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getById(@PathParam("id") Long id) {
        PotentialStudentPhone student = psPhoneService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @POST
    public Response create(@Valid PotentialStudentPhone psp) {

        psPhoneService.save(psp);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(psp.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(psp)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid PotentialStudentPhone psp) {

        PotentialStudentPhone student = psPhoneService.update(psp, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response delete(@PathParam("id") Long id) {
        psPhoneService.delete(id);

        return Response
                .ok()
                .build();
    }
}
