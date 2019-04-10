package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.PotentialStudentPopup;
import com.github.denrion.mef_marketing.service.PotentialStudentPopupService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("popup")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PotentialStudentPopupResource {

    @Inject
    PotentialStudentPopupService psPopupService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAll() {
        return Response
                .ok(psPopupService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getById(@PathParam("id") Long id) {
        PotentialStudentPopup student = psPopupService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @POST
    public Response create(@Valid PotentialStudentPopup psPopup) {

        psPopupService.save(psPopup);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(psPopup.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(psPopup)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid PotentialStudentPopup psPopup) {

        PotentialStudentPopup student = psPopupService.update(psPopup, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response delete(@PathParam("id") Long id) {
        psPopupService.delete(id);

        return Response
                .ok()
                .build();
    }
}
