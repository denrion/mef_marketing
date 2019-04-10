package com.github.denrion.mef_marketing.rest;

import com.github.denrion.mef_marketing.entity.PotentialStudentEform;
import com.github.denrion.mef_marketing.service.PotentialStudentEformService;

import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;

@Path("eform")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class PotentialStudentEformResource {

    @Inject
    PotentialStudentEformService psEformService;

    @Context
    UriInfo uriInfo;

    @GET
    public Response getAll() {
        return Response
                .ok(psEformService.getAll())
                .build();
    }

    @GET
    @Path("{id: \\d+}")
    public Response getById(@PathParam("id") Long id) {
        PotentialStudentEform student = psEformService.getById(id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @POST
    public Response create(@Valid PotentialStudentEform pse) {

        psEformService.save(pse);

        URI uri = uriInfo.getAbsolutePathBuilder()
                .path(pse.getId().toString())
                .build();

        return Response
                .created(uri)
                .entity(pse)
                .status(Response.Status.CREATED)
                .build();
    }

    @PUT
    @Path("{id: \\d+}")
    public Response update(@PathParam("id") Long id,
                           @Valid PotentialStudentEform pse) {

        PotentialStudentEform student = psEformService.update(pse, id)
                .orElseThrow(NotFoundException::new);

        return Response
                .ok(student)
                .build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public Response delete(@PathParam("id") Long id) {
        psEformService.delete(id);

        return Response
                .ok()
                .build();
    }
}
